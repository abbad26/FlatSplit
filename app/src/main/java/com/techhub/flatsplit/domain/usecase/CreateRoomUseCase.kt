package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.repository.FlatRoomRepository
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
    private val repository: FlatRoomRepository
) {

    suspend operator fun invoke(room: FlatRoom){ repository.createRoom(room) }
}