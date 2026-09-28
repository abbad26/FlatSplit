package com.techhub.flatsplit.presentation.settleup

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
import com.techhub.flatsplit.presentation.shimmer.SettleUpShimmer
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

    val userNames = uiState.users.associateBy(
        keySelector = { it.id },
        valueTransform = { it.name }
    )

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
                    key = { "${it.fromUserId}_${it.toUserId}" }
                ) { suggestion ->
                    val pendingSettlement =
                        uiState.settlements.firstOrNull { settlement ->
                            settlement.status == "PENDING" &&
                                    settlement.fromUserId == suggestion.fromUserId &&
                                    settlement.toUserId == suggestion.toUserId &&
                                    settlement.amount == suggestion.amount
                        }

                    SettlementSuggestionCard(
                        suggestion = suggestion,

                        fromName = userNames[suggestion.fromUserId]
                            ?: suggestion.fromUserId,

                        toName = userNames[suggestion.toUserId]
                            ?: suggestion.toUserId,

                        currentUserId = currentUserId,

                        pendingSettlement = pendingSettlement,

                        onSettle = {

                            selectedRoom?.let { room ->

                                viewModel.createSettlement(
                                    roomId = room.id,
                                    fromUserId = suggestion.fromUserId,
                                    toUserId = suggestion.toUserId,
                                    amount = suggestion.amount
                                )
                            }
                        },

                        onConfirm = {

                            pendingSettlement?.let { settlement ->

                                viewModel.confirmSettlement(
                                    settlementId = settlement.id
                                )
                            }
                        }
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