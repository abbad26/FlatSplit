package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.repository.SettlementRepository
import javax.inject.Inject

class DismissHomeRequestUseCase @Inject constructor(
    private val repository: SettlementRepository
) {
    suspend operator fun invoke(
        settlementId: String,
        userId: String
    ) {
        repository.dismissHomeRequest(
            settlementId = settlementId,
            userId = userId
        )
    }
}