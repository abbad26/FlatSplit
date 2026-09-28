package com.techhub.flatsplit.presentation.addexpenses

data class AddExpenseUiState(
    val isLoading: Boolean = false,
    val isLoadingMembers: Boolean = false,
    val members: List<ExpenseMember> = emptyList(),
    val isSuccess: Boolean = false,
    val error: String? = null
)