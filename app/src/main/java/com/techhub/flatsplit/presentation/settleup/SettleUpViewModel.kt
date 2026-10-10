package com.techhub.flatsplit.presentation.settleup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.usecase.GetRoomExpensesUseCase
import com.techhub.flatsplit.domain.usecase.GetUsersUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.CalculateBalanceUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.ConfirmSettlementUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.CreateSettlementUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.DismissHomeRequestUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.GenerateSettlementUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.GetRoomSettlementUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.MarkSettlementNotReceivedUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.StartSettlementPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettleUpViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getRoomExpensesUseCase: GetRoomExpensesUseCase,
    private val getRoomSettlementUseCase: GetRoomSettlementUseCase,
    private val calculateBalanceUseCase: CalculateBalanceUseCase,
    private val generateSettlementUseCase: GenerateSettlementUseCase,
    private val createSettlementUseCase: CreateSettlementUseCase,
    private val confirmSettlementUseCase: ConfirmSettlementUseCase,
    private val markSettlementNotReceivedUseCase: MarkSettlementNotReceivedUseCase,
    private val startSettlementPaymentUseCase: StartSettlementPaymentUseCase,
    private val dismissHomeRequestUseCase: DismissHomeRequestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettleUpUiState())

    val uiState: StateFlow<SettleUpUiState> =
        _uiState.asStateFlow()

    private var roomDataJob: Job? = null

    fun loadRoomSettlement(room: FlatRoom) {

        roomDataJob?.cancel()

        roomDataJob = viewModelScope.launch {

            try {

                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                // Load users of the selected room
                val users = getUsersUseCase(room.memberIds)

                // Observe expenses + settlements together
                combine(
                    getRoomExpensesUseCase(room.id),
                    getRoomSettlementUseCase(room.id)
                ) { expenses, settlements ->

                    val balances = calculateBalanceUseCase(
                        expenses = expenses,
                        members = room.memberIds,
                        settlements = settlements
                    )

                    val suggestions = generateSettlementUseCase(
                        balances = balances
                    )

                    SettleUpUiState(
                        isLoading = false,
                        expenses = expenses,
                        users = users,
                        settlements = settlements,
                        balances = balances,
                        suggestions = suggestions
                    )

                }.collect { state ->

                    _uiState.value = state
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                _uiState.value = SettleUpUiState(
                    isLoading = false,
                    error = e.message
                        ?: "Failed to load settlement data"
                )
            }
        }
    }

    fun createSettlement(
        roomId: String,
        fromUserId: String,
        toUserId: String,
        amount: Long,
        paymentMethod: String,
        transactionId: String? = null
    ) {

        viewModelScope.launch {

            try {
                val settlement = Settlement(
                    id = UUID.randomUUID().toString(),
                    roomId = roomId,
                    fromUserId = fromUserId,
                    toUserId = toUserId,
                    amount = amount,
                    paymentMethod = paymentMethod,
                    transactionId = transactionId,
                    status = "PENDING",
                    createdAt = Timestamp.now()
                )

                createSettlementUseCase(settlement)

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    error = e.message
                        ?: "Failed to create settlement"
                )
            }
        }
    }

    fun confirmSettlement(
        settlementId: String
    ) {

        viewModelScope.launch {

            try {

                confirmSettlementUseCase(
                    settlementId
                )

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    error = e.message
                        ?: "Failed to confirm settlement"
                )
            }
        }
    }


    fun markNotReceived(settlementId: String) {
        viewModelScope.launch {
            try {
                markSettlementNotReceivedUseCase(settlementId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                        ?: "Failed to update settlement."
                )
            }
        }
    }

    fun requestSettlement(
        roomId: String,
        fromUserId: String,
        toUserId: String,
        amount: Long
    ) {
        viewModelScope.launch {
            try {
                val settlement = Settlement(
                    id = UUID.randomUUID().toString(),
                    roomId = roomId,
                    fromUserId = fromUserId,
                    toUserId = toUserId,
                    amount = amount,
                    paymentMethod = "",
                    transactionId = null,
                    status = "REQUESTED",
                    createdAt = Timestamp.now(),
                    homeDismissedBy = emptyList()
                )

                createSettlementUseCase(settlement)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                        ?: "Failed to request payment."
                )
            }
        }
    }

    fun startSettlementPayment(
        settlementId: String,
        paymentMethod: String,
        transactionId: String? = null
    ) {
        viewModelScope.launch {
            try {
                startSettlementPaymentUseCase(
                    settlementId = settlementId,
                    paymentMethod = paymentMethod,
                    transactionId = transactionId
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                        ?: "Failed to update payment request."
                )
            }
        }
    }

    fun dismissHomeRequest(
        settlementId: String,
        userId: String
    ) {
        viewModelScope.launch {
            try {
                dismissHomeRequestUseCase(
                    settlementId = settlementId,
                    userId = userId
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                        ?: "Failed to dismiss request notification."
                )
            }
        }
    }

    override fun onCleared() {
        roomDataJob?.cancel()
        super.onCleared()
    }
}