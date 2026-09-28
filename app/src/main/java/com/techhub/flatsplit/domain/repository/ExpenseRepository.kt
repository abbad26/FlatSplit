package com.techhub.flatsplit.domain.repository

import com.techhub.flatsplit.domain.model.settlement.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun addExpense(expense: Expense)

    fun getRoomExpenses(roomId: String): Flow<List<Expense>>
}