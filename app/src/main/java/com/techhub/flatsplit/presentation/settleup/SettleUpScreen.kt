package com.techhub.flatsplit.presentation.settleup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.presentation.RoomSelectionViewModel
import com.techhub.flatsplit.presentation.components.AppError
import com.techhub.flatsplit.presentation.home.FlatSwitcherDropdown
import com.techhub.flatsplit.presentation.settleup.upi.createUpiPaymentIntent
import com.techhub.flatsplit.presentation.settleup.upi.parseUpiPaymentResult
import com.techhub.flatsplit.presentation.shimmer.SettleUpShimmer
import android.content.ActivityNotFoundException
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun SettleUpScreen(
    viewModel: SettleUpViewModel = hiltViewModel(),
    roomSelectionViewModel: RoomSelectionViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    val dimens = AppTheme.dimensions

    val roomState by roomSelectionViewModel.uiState.collectAsState()

    val currentUserId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid
            ?: return

    val context = LocalContext.current

    var pendingRoomId by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    var pendingFromUserId by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    var pendingToUserId by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    var pendingAmount by rememberSaveable {
        mutableStateOf(0L)
    }
    var pendingExistingSettlementId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Load all flats of the logged-in user
    LaunchedEffect(currentUserId) {
        roomSelectionViewModel.loadRooms(currentUserId)
    }


    val selectedRoom = roomState.selectedRoom

    LaunchedEffect(selectedRoom?.id) {
        selectedRoom?.let { room ->
            viewModel.loadRoomSettlement(room)
        }
    }

    val currentBalance = uiState.balances
        .firstOrNull { it.userId == currentUserId }
        ?.balance
        ?: 0L

//    val userNames = uiState.users.associateBy(
//        keySelector = { it.id },
//        valueTransform = { it.name }
//    )

    val usersById = uiState.users.associateBy { it.id }

    val upiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        Log.e(
            "FlatSplitUPI",
            "CALLBACK_ENTERED: resultCode=${result.resultCode}, " +
                    "dataReturned=${result.data != null}, " +
                    "pendingRoomExists=${pendingRoomId != null}, " +
                    "pendingAmount=${pendingAmount > 0L}, " +
                    "extraKeys=${result.data?.extras?.keySet()}"
        )

        val pending = if (
            pendingRoomId != null &&
            pendingFromUserId != null &&
            pendingToUserId != null &&
            pendingAmount > 0L
        ) {
            PendingUpiSettlement(
                roomId = pendingRoomId!!,
                fromUserId = pendingFromUserId!!,
                toUserId = pendingToUserId!!,
                amount = pendingAmount,
                existingSettlementId = pendingExistingSettlementId
            )
        } else {
            null
        }
        // Clear the saved details only after copying them locally.
        pendingRoomId = null
        pendingFromUserId = null
        pendingToUserId = null
        pendingAmount = 0L
        pendingExistingSettlementId = null

        if (pending == null) {
            Log.e("FlatSplitUPI", "No pending settlement details were restored.")
            return@rememberLauncherForActivityResult
        }

        val upiResult = parseUpiPaymentResult(result.data)
        val responseString = result.data?.getStringExtra("response")

        val responseKeys = responseString
            ?.split('&', ';')
            ?.mapNotNull { part ->
                val key = part.substringBefore('=').trim()
                key.takeIf { part.contains('=') && it.isNotBlank() }
            }

        val extraNames = result.data
            ?.extras
            ?.keySet()
            ?.joinToString()

        val uriQueryNames = result.data
            ?.data
            ?.queryParameterNames
            ?.joinToString()

        val safeStatusFields = listOf(
            "Status",
            "status",
            "STATUS",
            "txnStatus",
            "responseCode",
            "ResponseCode"
        ).mapNotNull { key ->
            result.data?.extras?.get(key)?.toString()
                ?.let { "$key=$it" }
        }

        Log.e(
            "FlatSplitUPI",
            "RETURNED_DETAILS: " +
                    "resultCode=${result.resultCode}, " +
                    "extraNames=$extraNames, " +
                    "responseExtraPresent=${responseString != null}, " +
                    "uriScheme=${result.data?.data?.scheme}, " +
                    "uriQueryNames=$uriQueryNames, " +
                    "statusFields=$safeStatusFields, " +
                    "parsedStatus=${upiResult.status}, " +
                    "parsedResponseCode=${upiResult.responseCode}"
        )

        val status = upiResult.status
            ?.trim()
            ?.uppercase(Locale.ROOT)

        val responseCode = upiResult.responseCode?.trim()

        val paymentReportedSuccessful =
            status in setOf("SUCCESS", "SUCCESSFUL") &&
                    (responseCode.isNullOrBlank() || responseCode == "00")

        if (paymentReportedSuccessful) {
            val existingSettlementId = pending.existingSettlementId

            if (existingSettlementId != null) {
                // Update the creditor's existing request.
                viewModel.startSettlementPayment(
                    settlementId = existingSettlementId,
                    paymentMethod = "UPI",
                    transactionId = upiResult.transactionId
                        ?: upiResult.transactionRef                )
            } else {
                // No prior request: create a new settlement.
                viewModel.createSettlement(
                    roomId = pending.roomId,
                    fromUserId = pending.fromUserId,
                    toUserId = pending.toUserId,
                    amount = pending.amount,
                    paymentMethod = "UPI",
                    transactionId = upiResult.transactionId
                        ?: upiResult.transactionRef                )
            }

            Toast.makeText(
                context,
                "Payment reported successful. Awaiting recipient confirmation.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val message = when (status) {
                "FAILURE", "FAILED" ->
                    "The UPI app reported payment failure. No settlement was created."

                "SUBMITTED", "PENDING" ->
                    "Payment status is pending. Check your UPI app before retrying."

                else ->
                    "No successful payment result was received. Check your transaction history."
            }

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    when {

        roomState.isLoading || uiState.isLoading -> {
            SettleUpShimmer()
        }
        roomState.error != null -> {

            AppError(
                message = roomState.error!!,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        uiState.error != null -> {

            AppError(
                message = uiState.error!!,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        roomState.selectedRoom == null -> {
            // empty ui
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimens.large, vertical = dimens.large),
                verticalArrangement = Arrangement.spacedBy(dimens.large)
            ) {

                item {
                    FlatSwitcherDropdown(
                        rooms = roomState.rooms,
                        selectedRoom = roomState.selectedRoom,
                        onRoomSelected = { room ->
                            roomSelectionViewModel.selectRoom(room)
                        }
                    )
                }
                item {

                    BalanceSummaryCard(
                        balance = currentBalance
                    )
                }

                item {

                    Text(
                        text = "SETTLEMENT SUGGESTIONS",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(
                    items = uiState.suggestions,
                    key = {
                        "${it.fromUserId}_${it.toUserId}"
                    }
                ) { suggestion ->

                    val pendingSettlement = uiState.settlements.firstOrNull { settlement ->
                        settlement.status in setOf("REQUESTED", "PENDING") &&
                                settlement.fromUserId == suggestion.fromUserId &&
                                settlement.toUserId == suggestion.toUserId &&
                                settlement.amount == suggestion.amount
                    }
                    val fromUser =
                        usersById[suggestion.fromUserId]

                    val toUser =
                        usersById[suggestion.toUserId]

                    SettlementSuggestionCard(
                        suggestion = suggestion,

                        fromName = fromUser?.name
                            ?: suggestion.fromUserId,

                        toName = toUser?.name
                            ?: suggestion.toUserId,

                        currentUserId = currentUserId,

                        pendingSettlement = pendingSettlement,

                        toUpiId = toUser?.upiId,

                        onPayWithUpi = {

                            val room = selectedRoom
                            val recipient = toUser
                            val recipientUpiId = recipient?.upiId?.trim()

                            if (room == null || recipient == null ||
                                recipientUpiId.isNullOrBlank()
                            ) {
                                Toast.makeText(
                                    context,
                                    "The recipient has not saved a UPI ID.",
                                    Toast.LENGTH_LONG
                                ).show()

                            } else {

                                val paymentIntent = createUpiPaymentIntent(
                                    upiId = recipientUpiId,
                                    payeeName = recipient.name,
                                    amountPaise = suggestion.amount,
                                    note = "FlatSplit settlement"
                                )

                                pendingRoomId = room.id
                                pendingFromUserId = suggestion.fromUserId
                                pendingToUserId = suggestion.toUserId
                                pendingAmount = suggestion.amount

                                pendingExistingSettlementId = pendingSettlement
                                    ?.takeIf { it.status == "REQUESTED" }
                                    ?.id
                                try {

                                    android.util.Log.e(
                                        "FlatSplitUPI",
                                        "LAUNCHING: action=${paymentIntent.action}, " +
                                                "scheme=${paymentIntent.data?.scheme}, " +
                                                "amount=${paymentIntent.data?.getQueryParameter("am")}, " +
                                                "recipientPresent=${!paymentIntent.data
                                                    ?.getQueryParameter("pa").isNullOrBlank()}"
                                    )
                                    upiLauncher.launch(paymentIntent)
                                } catch (e: ActivityNotFoundException) {
                                    pendingRoomId = null
                                    pendingFromUserId = null
                                    pendingToUserId = null
                                    pendingAmount = 0L
                                    pendingExistingSettlementId = null

                                    Toast.makeText(
                                        context,
                                        "No compatible UPI app was found.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },

                        onPayWithCash = {
                            val existingSettlement = pendingSettlement

                            if (existingSettlement?.status == "REQUESTED") {
                                // Reuse the creditor's request.
                                viewModel.startSettlementPayment(
                                    settlementId = existingSettlement.id,
                                    paymentMethod = "CASH",
                                    transactionId = null
                                )
                            } else {
                                // No request exists, so create a new pending settlement.
                                selectedRoom?.let { room ->
                                    viewModel.createSettlement(
                                        roomId = room.id,
                                        fromUserId = suggestion.fromUserId,
                                        toUserId = suggestion.toUserId,
                                        amount = suggestion.amount,
                                        paymentMethod = "CASH",
                                        transactionId = null
                                    )
                                }
                            }
                        },

                        onConfirm = {
                            pendingSettlement?.let { settlement ->

                                viewModel.confirmSettlement(
                                    settlementId = settlement.id
                                )
                            }
                        },
                        onNotReceived = {
                            pendingSettlement?.let { settlement ->
                                viewModel.markNotReceived(settlementId = settlement.id)
                            }
                        },
                        onRequest = {
                            selectedRoom?.let { room ->
                                viewModel.requestSettlement(
                                    roomId = room.id,
                                    fromUserId = suggestion.fromUserId,
                                    toUserId = suggestion.toUserId,
                                    amount = suggestion.amount
                                )
                            }
                        },
                    )
                }
                // Bottom spacing
                item {

                    Spacer(
                        modifier = Modifier.height(dimens.large)
                    )
                }
            }

        }
    }
}


private data class PendingUpiSettlement(
    val roomId: String,
    val fromUserId: String,
    val toUserId: String,
    val amount: Long,
    val existingSettlementId: String? = null
)