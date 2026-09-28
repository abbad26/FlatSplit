package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.repository.FlatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserRoomsUseCase @Inject constructor(
    private val repository: FlatRoomRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Flow<List<FlatRoom>> {

        return repository.getUserRooms(userId = userId)
    }
}