package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense){
        repository.addExpense(expense = expense)
    }
}