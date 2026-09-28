package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.model.settlement.MemberBalance
import com.techhub.flatsplit.domain.model.settlement.Settlement
import javax.inject.Inject

class CalculateBalanceUseCase @Inject constructor() {

    operator fun invoke(
        expenses: List<Expense>,
        members: List<String>,
        settlements: List<Settlement> = emptyList()
    ): List<MemberBalance> {


        if (members.isEmpty()){
            return emptyList()
        }

        val balances = members
            .associateWith { 0L }
            .toMutableMap()

        for (expense in expenses){
            val splitMembers = expense.splitBetween

            if (expense.splitBetween.isEmpty()) {
                continue
            }

            val memberCount = splitMembers.size
            val baseShare = expense.amount / memberCount
            val remainder = expense.amount % memberCount

            splitMembers.forEachIndexed { index, member ->
                val memberShare =
                    baseShare + if ( index < remainder) 1L else 0L

                if (member in balances) {
                    balances[member] =
                        balances.getValue(member) - memberShare
                }

            }

            // Person who paid gets the full amount back
            if (expense.paidBy in balances) {
                balances[expense.paidBy] =
                    balances.getValue(expense.paidBy) + expense.amount
            }
        }

        settlements
            .filter { it.status == "CONFIRMED" }
            .forEach { settlement ->

                // Debtor has paid their debt
                if (settlement.fromUserId in balances) {
                    balances[settlement.fromUserId] =
                        balances.getValue(
                            settlement.fromUserId
                        ) + settlement.amount
                }

                // Creditor has received the money
                if (settlement.toUserId in balances) {
                    balances[settlement.toUserId] =
                        balances.getValue(
                            settlement.toUserId
                        ) - settlement.amount
                }
            }


        return balances.map { (userId, balance) ->
            MemberBalance(
                userId = userId,
                balance = balance
            )
        }
    }
}
