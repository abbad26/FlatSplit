package com.techhub.flatsplit.domain.usecase.manageflatusecase

import com.techhub.flatsplit.domain.repository.FlatRoomRepository
import javax.inject.Inject

class DeleteFlatUseCase @Inject constructor(
    private val repository: FlatRoomRepository
) {
    suspend operator fun invoke(
        roomId: String
    ){
        repository.deleteRoom(roomId)
    }
}