package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.model.settlement.MemberBalance
import com.techhub.flatsplit.domain.model.settlement.SettlementSuggestion
import javax.inject.Inject
import kotlin.math.abs

class GenerateSettlementUseCase @Inject constructor() {

    operator fun invoke(
        balances: List<MemberBalance>
    ): List<SettlementSuggestion> {

        val debtors = balances
            .filter { it.balance < 0 }
            .map { it.userId to abs(it.balance) }
            .toMutableList()

        val creditors = balances
            .filter { it.balance > 0 }
            .map { it.userId to it.balance }
            .toMutableList()

        val suggestions = mutableListOf<SettlementSuggestion>()

        var debtorIndex = 0
        var creditorIndex = 0

        while (
            debtorIndex < debtors.size && creditorIndex < creditors.size
        ) {
            val debtor = debtors[debtorIndex]
            val creditor = creditors[creditorIndex]

            val amount = minOf(debtor.second, creditor.second)

            suggestions.add(
                SettlementSuggestion(
                    fromUserId = debtor.first,
                    toUserId = creditor.first,
                    amount = amount
                )
            )

            val remainingDebt = debtor.second - amount
            val remainingCredit = creditor.second - amount

            debtors[debtorIndex] = debtor.first to remainingDebt
            creditors[creditorIndex] = creditor.first to remainingCredit

            if (remainingDebt == 0L) {
                debtorIndex++
            }

            if (remainingCredit == 0L){
                creditorIndex++
            }
        }

        return suggestions
    }
}