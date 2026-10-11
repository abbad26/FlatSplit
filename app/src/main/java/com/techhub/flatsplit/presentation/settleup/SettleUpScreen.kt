package com.techhub.flatsplit.presentation.settleup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.presentation.RoomSelectionViewModel
import com.techhub.flatsplit.presentation.components.AppError
import com.techhub.flatsplit.presentation.home.FlatSwitcherDropdown
import com.techhub.flatsplit.presentation.shimmer.SettleUpShimmer
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SettleUpScreen(
    viewModel: SettleUpViewModel = hiltViewModel(),
    roomSelectionViewModel: RoomSelectionViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val roomState by roomSelectionViewModel.uiState.collectAsState()
    val dimens = AppTheme.dimensions
    val context = LocalContext.current
    var showPaymentHistory by rememberSaveable { mutableStateOf(false) }

    val currentUserId = FirebaseAuth.getInstance()
        .currentUser
        ?.uid
        ?: return

    // Load all flats belonging to the logged-in user.
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

    val usersById = uiState.users.associateBy { it.id }

    // Display payment records from last 30 days
    val thirtyDaysAgoMillis = System.currentTimeMillis() - (30L * 24L * 60L * 60L * 1000L)
    val paymentHistory = uiState.settlements
        .filter { settlement ->
            settlement.status in setOf("PENDING", "CONFIRMED", "NOT_RECEIVED") &&
                    (settlement.confirmedAt ?: settlement.createdAt).toDate().time >= thirtyDaysAgoMillis
        }
        .sortedByDescending { settlement ->
            (settlement.confirmedAt ?: settlement.createdAt).toDate().time
        }

    when {
        roomState.isLoading || uiState.isLoading -> {
            SettleUpShimmer()
        }

        roomState.error != null -> {
            AppError(
                message = roomState.error!!,
                modifier = Modifier.fillMaxSize()
            )
        }

        uiState.error != null -> {
            AppError(
                message = uiState.error!!,
                modifier = Modifier.fillMaxSize()
            )
        }

        selectedRoom == null -> {
            EmptySettleUpState()
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = dimens.large,
                        vertical = dimens.large
                    ),
                verticalArrangement = Arrangement.spacedBy(dimens.large)
            ) {
                item {
                    FlatSwitcherDropdown(
                        rooms = roomState.rooms,
                        selectedRoom = selectedRoom,
                        onRoomSelected = { room ->
                            roomSelectionViewModel.selectRoom(room)
                        }
                    )
                }

                if (showPaymentHistory){
                    // Dedicated history view, opened from the History action beside the suggestions title.
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimens.small)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "PAYMENT HISTORY",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { showPaymentHistory = false }) {
                                    Text(
                                        text = "← Back",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Text(
                                text = "Payments recorded in the last 30 days",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (paymentHistory.isEmpty()) {
                        item {
                            Text(
                                text = "No recorded payments in the last 30 days.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = dimens.small)
                            )
                        }
                    } else {
                        items(
                            items = paymentHistory,
                            key = { settlement ->
                                "history-${settlement.id.ifBlank {
                                    "${settlement.fromUserId}_${settlement.toUserId}_${settlement.createdAt.seconds}"
                                }}"
                            }
                        ) { settlement ->
                            val payerName = usersById[settlement.fromUserId]?.name
                                ?: settlement.fromUserId
                            val recipientName = usersById[settlement.toUserId]?.name
                                ?: settlement.toUserId
                            val historyDate = SimpleDateFormat(
                                "dd MMM yyyy, hh:mm a",
                                Locale.getDefault()
                            ).format((settlement.confirmedAt ?: settlement.createdAt).toDate())

                            PaymentHistoryItem(
                                payerName = payerName,
                                recipientName = recipientName,
                                amountPaise = settlement.amount,
                                paymentMethod = settlement.paymentMethod,
                                status = settlement.status,
                                dateText = historyDate
                            )
                        }
                    }
                } else {
                    item {
                        BalanceSummaryCard(balance = currentBalance)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SETTLEMENT SUGGESTIONS",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { showPaymentHistory = true }) {
                                Text(
                                    text = "History →",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    )
                            }
                        }
                    }

                items(
                    items = uiState.suggestions,
                    key = { "${it.fromUserId}_${it.toUserId}" }
                ) { suggestion ->
                    val pendingSettlement = uiState.settlements.firstOrNull { settlement ->
                        settlement.status in setOf("REQUESTED", "PENDING") &&
                                settlement.fromUserId == suggestion.fromUserId &&
                                settlement.toUserId == suggestion.toUserId &&
                                settlement.amount == suggestion.amount
                    }

                    val fromUser = usersById[suggestion.fromUserId]
                    val toUser = usersById[suggestion.toUserId]

                    SettlementSuggestionCard(
                        suggestion = suggestion,
                        fromName = fromUser?.name ?: suggestion.fromUserId,
                        toName = toUser?.name ?: suggestion.toUserId,
                        currentUserId = currentUserId,
                        pendingSettlement = pendingSettlement,
                        onPayWithCash = {
                            val existingSettlement = pendingSettlement

                            if (existingSettlement?.status == "REQUESTED") {
                                // Reuse the creditor's request and mark the reported method.
                                viewModel.startSettlementPayment(
                                    settlementId = existingSettlement.id,
                                    paymentMethod = "CASH",
                                    transactionId = null
                                )
                            } else {
                                // Create a pending record; the creditor must confirm receipt.
                                viewModel.createSettlement(
                                    roomId = selectedRoom.id,
                                    fromUserId = suggestion.fromUserId,
                                    toUserId = suggestion.toUserId,
                                    amount = suggestion.amount,
                                    paymentMethod = "CASH",
                                    transactionId = null
                                )
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
                                viewModel.markNotReceived(
                                    settlementId = settlement.id
                                )
                            }
                        },
                        onRequest = {
                            viewModel.requestSettlement(
                                roomId = selectedRoom.id,
                                fromUserId = suggestion.fromUserId,
                                toUserId = suggestion.toUserId,
                                amount = suggestion.amount
                            )
                        },
                        onReportManualUpiPayment = {
                            when (pendingSettlement?.status) {
                                "REQUESTED" -> {
                                    viewModel.startSettlementPayment(
                                        settlementId = pendingSettlement.id,
                                        paymentMethod = "UPI",
                                        transactionId = null
                                    )
                                }

                                null -> {
                                    viewModel.createSettlement(
                                        roomId = selectedRoom.id,
                                        fromUserId = suggestion.fromUserId,
                                        toUserId = suggestion.toUserId,
                                        amount = suggestion.amount,
                                        paymentMethod = "UPI",
                                        transactionId = null
                                    )
                                }

                                else -> {
                                    Toast.makeText(
                                        context,
                                        "A payment is already awaiting confirmation.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                }
                    item {
                    Spacer(modifier = Modifier.height(dimens.large))
                    }
                }
            }
        }
    }
}
