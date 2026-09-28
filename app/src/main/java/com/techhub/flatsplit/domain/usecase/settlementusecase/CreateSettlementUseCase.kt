package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.repository.SettlementRepository
import javax.inject.Inject

class CreateSettlementUseCase @Inject constructor(
    private val repository: SettlementRepository
) {

    suspend operator fun invoke(
        settlement: Settlement
    ) {
        repository.createSettlement(settlement)
    }
}