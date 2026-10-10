package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.repository.SettlementRepository
import javax.inject.Inject

class StartSettlementPaymentUseCase @Inject constructor(
    private val repository: SettlementRepository
) {
    suspend operator fun invoke(
        settlementId: String,
        paymentMethod: String,
        transactionId: String?
    ) {
        repository.startSettlementPayment(
            settlementId = settlementId,
            paymentMethod = paymentMethod,
            transactionId = transactionId
        )
    }
}