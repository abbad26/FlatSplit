package com.techhub.flatsplit.domain.repository

import com.techhub.flatsplit.domain.model.FlatRoom
import kotlinx.coroutines.flow.Flow

interface FlatRoomRepository {

    suspend fun createRoom(room: FlatRoom)

    suspend fun joinRoom(
        userId: String,
        inviteCode: String
    )

    fun getUserRooms(
        userId: String
    ): Flow<List<FlatRoom>>

    suspend fun removeMember(
        roomId: String,
        userId: String
    )

    suspend fun deleteRoom(
        roomId: String
    )
}