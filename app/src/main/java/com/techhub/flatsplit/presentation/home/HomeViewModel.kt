package com.techhub.flatsplit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.usecase.GetRoomExpensesUseCase
import com.techhub.flatsplit.domain.usecase.GetUsersUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.CalculateBalanceUseCase
import com.techhub.flatsplit.domain.usecase.settlementusecase.GetRoomSettlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeUiState(
    val isLoading: Boolean = false,
    val expenses: List<Expense> = emptyList(),
    val users: List<User> = emptyList(),

    val settlements: List<Settlement> = emptyList(),

    val totalSpent: Long = 0L,
    val yourPaid: Long = 0L,
    val currentBalance: Long = 0L,

    val error: String? = null
)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRoomSettlementUseCase: GetRoomSettlementUseCase,
    private val getRoomExpensesUseCase: GetRoomExpensesUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val calculateBalanceUseCase: CalculateBalanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    private var roomDataJob: Job? = null

    fun loadRoomData(
        room: FlatRoom,
        currentUserId: String
    ) {

        roomDataJob?.cancel()

        roomDataJob = viewModelScope.launch {

            try {

                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                val users =
                    getUsersUseCase(room.memberIds)

                combine(
                    getRoomExpensesUseCase(room.id),
                    getRoomSettlementUseCase(room.id)
                ) { expenses, settlements ->

                    val balances =
                        calculateBalanceUseCase(
                            expenses = expenses,
                            members = room.memberIds,
                            settlements = settlements
                        )

                    val currentBalance = balances
                            .firstOrNull {
                                it.userId == currentUserId
                            }
                            ?.balance
                            ?: 0L

                    val totalSpent = expenses.sumOf { it.amount }

                    val yourPaid = expenses
                            .filter { it.paidBy == currentUserId }
                            .sumOf { it.amount }

                    HomeUiState(
                        isLoading = false,
                        expenses = expenses,
                        users = users,
                        settlements = settlements,
                        totalSpent = totalSpent,
                        yourPaid = yourPaid,
                        currentBalance = currentBalance
                    )

                }.collect { state ->

                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = e.message
                        ?: "Failed to load home data"
                )
            }
        }
    }
    override fun onCleared() {
        roomDataJob?.cancel()
        super.onCleared()
    }
}