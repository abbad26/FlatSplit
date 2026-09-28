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
        firestore
            .collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
}