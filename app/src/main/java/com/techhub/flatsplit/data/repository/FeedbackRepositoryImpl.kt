package com.techhub.flatsplit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.domain.model.Feedback
import com.techhub.flatsplit.domain.repository.FeedbackRepository
import kotlinx.coroutines.tasks.await

class FeedbackRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FeedbackRepository {

    override suspend fun submitFeedback(feedback: Feedback) {
        val user = firebaseAuth.currentUser
            ?: throw IllegalStateException(
                "Your session has expired. Please sign in again."
            )

        val data = hashMapOf<String, Any>(
            "userId" to user.uid,
            "userEmail" to (user.email ?: ""),
            "category" to feedback.category,
            "message" to feedback.message.trim(),
            "status" to "NEW",
            "appName" to "FlatSplit",
            "createdAt" to FieldValue.serverTimestamp()
        )

        feedback.rating?.let { rating ->
            if (rating in 1..5) {
                data["rating"] = rating
            }
        }

        // Wait for Firestore to confirm the writes.
        firestore.collection("feedback")
            .add(data)
            .await()
    }
}