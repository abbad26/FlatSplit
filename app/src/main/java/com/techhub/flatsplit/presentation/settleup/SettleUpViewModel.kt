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
import com.techhub.flatsplit.domain.usecase.settlementusecase.GenerateSettlementUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.GetRoomSettlementUseCase
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
    private val confirmSettlementUseCase: ConfirmSettlementUseCase
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

                    val suggestions =
                        generateSettlementUseCase(
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

    override fun onCleared() {
        roomDataJob?.cancel()
        super.onCleared()
    }
}