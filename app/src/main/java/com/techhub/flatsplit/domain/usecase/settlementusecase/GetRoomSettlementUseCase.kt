package com.techhub.flatsplit.domain.usecase.settlementusecase

import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.repository.SettlementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomSettlementUseCase @Inject constructor(
    private val repository: SettlementRepository
) {
    suspend operator fun invoke(
        roomId: String
    ) : Flow<List<Settlement>> {
        return repository.getRoomSettlements(roomId)
    }
}