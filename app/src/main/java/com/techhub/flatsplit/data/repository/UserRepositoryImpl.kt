package com.techhub.flatsplit.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUser(userId: String): User? {

        return firestore
            .collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
    }

    override suspend fun getUsers(
        userIds: List<String>
    ): List<User> {

        if (userIds.isEmpty()) {
            return emptyList()
        }
        return userIds.mapNotNull { userId ->
            getUser(userId)
        }
    }

    override suspend fun saveUser(user: User) {
        val userRef = firestore
            .collection("users")
            .document(user.id)

        val snapshot = userRef.get().await()

        if (snapshot.exists()) {
            // Update Google profile fields only.
            // Preserve the existing UPI ID.
            userRef.update(
                mapOf(
                    "id" to user.id,
                    "name" to user.name,
                    "email" to user.email,
                    "photoUrl" to user.photoUrl
                )
            ).await()
        } else {
            // First-time user: create the complete document.
            userRef.set(user).await()
        }
    }
    override suspend fun updateUpiId(userId: String, upiId: String) {
        firestore
            .collection("users")
            .document(userId)
            .update("upiId", upiId.trim())
            .await()
    }
}