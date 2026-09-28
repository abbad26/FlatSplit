package com.techhub.flatsplit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.techhub.flatsplit.domain.model.UserAuth
import com.techhub.flatsplit.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override suspend fun signInWithGoogle(idToken: String): Result<UserAuth> {

        return try {

            val credential = GoogleAuthProvider
                .getCredential(idToken, null)

            val authResult = firebaseAuth
                .signInWithCredential(credential)
                .await()

            val firebaseUser = authResult.user
                ?: return Result.failure(
                    Exception("Firebase user is null")
                )

            val user = UserAuth(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName.orEmpty(),
                email = firebaseUser.email.orEmpty(),
                profileImage = firebaseUser.photoUrl?.toString()
            )

            Result.success(user)
        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun signOut() {
        return firebaseAuth.signOut()
    }

}