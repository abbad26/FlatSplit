package com.techhub.flatsplit.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.repository.SettlementRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SettlementRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SettlementRepository {

    override suspend fun createSettlement(
        settlement: Settlement
    ) {
        firestore
            .collection("settlements")
            .document(settlement.id)
            .set(settlement)
            .await()
    }

    override fun getRoomSettlements(
        roomId: String
    ): Flow<List<Settlement>> = callbackFlow {

        val listener = firestore
            .collection("settlements")
            .whereEqualTo("roomId", roomId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val settlements =
                    snapshot?.documents?.mapNotNull { document ->
                        document.toObject(Settlement::class.java)
                    } ?: emptyList()

                trySend(settlements)
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun confirmSettlement(
        settlementId: String
    ) {
        firestore
            .collection("settlements")
            .document(settlementId)
            .update(
                mapOf(
                    "status" to "CONFIRMED",
                    "confirmedAt" to com.google.firebase.Timestamp.now()
                )
            )
            .await()
    }
}