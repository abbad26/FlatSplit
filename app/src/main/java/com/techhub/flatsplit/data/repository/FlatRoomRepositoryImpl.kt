package com.techhub.flatsplit.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.repository.FlatRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FlatRoomRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FlatRoomRepository {

    override suspend fun createRoom(room: FlatRoom) {
        firestore
            .collection("rooms")
            .document(room.id)
            .set(room)
            .await()
    }

    override suspend fun joinRoom(userId: String, inviteCode: String) {
        val snapshot = firestore
            .collection("rooms")
            .whereEqualTo("inviteCode", inviteCode)
            .get()
            .await()

        if (snapshot.isEmpty) {
            throw Exception("Room not found")
        }

        val document = snapshot.documents.first()

        firestore
            .collection("rooms")
            .document(document.id)
            .update(
                "memberIds",
                com.google.firebase.firestore.FieldValue.arrayUnion(userId)
            )
            .await()
    }

    override fun getUserRooms(userId: String): Flow<List<FlatRoom>> = callbackFlow {

        val listener = firestore
            .collection("rooms")
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val rooms = snapshot
                    ?.documents
                    ?.mapNotNull { document ->
                        document.toObject(FlatRoom::class.java)
                    }
                    ?: emptyList()

                trySend(rooms)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun removeMember(roomId: String, userId: String) {

        firestore
            .collection("rooms")
            .document(roomId)
            .update(
                "memberIds",
                FieldValue.arrayRemove(userId)
            )
            .await()
    }

    override suspend fun deleteRoom(
        roomId: String
    ) {
        val expenseSnapshot = firestore
            .collection("expenses")
            .whereEqualTo("roomId", roomId)
            .get()
            .await()

        val settlementSnapshot = firestore
            .collection("settlements")
            .whereEqualTo("roomId", roomId)
            .get()
            .await()

        val batch = firestore.batch()

        expenseSnapshot.documents.forEach { document ->
            batch.delete(document.reference)
        }

        settlementSnapshot.documents.forEach { document ->
            batch.delete(document.reference)
        }

        batch.delete(
            firestore
                .collection("rooms")
                .document(roomId)
        )
        batch.commit().await()
    }
}