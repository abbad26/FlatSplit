package com.techhub.flatsplit.presentation.addexpenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.usecase.AddExpenseUseCase
import com.techhub.flatsplit.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event: SharedFlow<String> = _event.asSharedFlow()

    fun loadMembers(memberIds: List<String>) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoadingMembers = true,
                    error = null
                )

                val users = getUsersUseCase(memberIds)

                val members = users.map { user ->
                    ExpenseMember(
                        id = user.id,
                        name = user.name
                    )
                }

                _uiState.value = _uiState.value.copy(
                    isLoadingMembers = false,
                    members = members,
                    error = null
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMembers = false,
                    error = e.message ?: "Failed to load flatmates"
                )
            }
        }
    }

    fun addExpense(
        roomId: String,
        title: String,
        amountPaise: Long,
        category: String,
        paidBy: String,
        splitBetween: List<String>
    ) {
        viewModelScope.launch {
            try {

                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                val expense = Expense(
                    id = UUID.randomUUID().toString(),
                    roomId = roomId,
                    title = title,
                    amount = amountPaise,
                    category = category,
                    paidBy = paidBy,
                    splitBetween = splitBetween,
                    createdAt = Timestamp.now()
                )

                addExpenseUseCase(expense)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )

                _event.emit("Expense added successfully")

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to add expense"
                )
            }
        }
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(
            isSuccess = false
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }
}