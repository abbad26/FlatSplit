package com.techhub.flatsplit.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.repository.ExpenseRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ExpenseRepository {
    override suspend fun addExpense(expense: Expense) {
        firestore
            .collection("expenses")
            .document(expense.id)
            .set(expense)
            .await()
    }

    override fun getRoomExpenses(roomId: String): Flow<List<Expense>> = callbackFlow {

        val listener = firestore
            .collection("expenses")
            .whereEqualTo("roomId", roomId)
            .addSnapshotListener { snapshots, error ->

                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }

                val expenses = snapshots?.documents?.mapNotNull {
                    it.toObject(Expense::class.java)
                } ?: emptyList()

                trySend(expenses)
            }

        awaitClose {
            listener.remove()
        }

    }

}