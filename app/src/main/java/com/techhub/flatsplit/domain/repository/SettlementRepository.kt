package com.techhub.flatsplit.domain.repository

import com.techhub.flatsplit.domain.model.settlement.Settlement
import kotlinx.coroutines.flow.Flow

interface SettlementRepository {

    suspend fun createSettlement(
        settlement: Settlement
    )

    fun getRoomSettlements(
        roomId: String
    ): Flow<List<Settlement>>

    suspend fun confirmSettlement(settlementId: String)

    suspend fun markNotReceived(settlementId: String)

    suspend fun startSettlementPayment(
        settlementId: String,
        paymentMethod: String,
        transactionId: String?
    )

    suspend fun dismissHomeRequest(
        settlementId: String,
        userId: String
    )
}