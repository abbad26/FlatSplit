package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {

    operator fun invoke(
        roomId: String
    ): Flow<List<Expense>> {
        return repository.getRoomExpenses(roomId)
    }
}