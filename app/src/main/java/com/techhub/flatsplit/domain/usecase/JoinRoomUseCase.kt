package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.repository.FlatRoomRepository
import javax.inject.Inject

class JoinRoomUseCase @Inject constructor(
    private val repository: FlatRoomRepository
) {

    suspend operator fun invoke(
        userId: String,
        inviteCode: String
    ) {
        repository.joinRoom(userId = userId, inviteCode = inviteCode)
    }
}