package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.repository.SettlementRepository
import javax.inject.Inject

class ConfirmSettlementUseCase @Inject constructor(
    private val repository: SettlementRepository
) {

    suspend operator fun invoke(
        settlementId: String
    ) {
        repository.confirmSettlement(settlementId)
    }
}