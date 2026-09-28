package com.techhub.flatsplit.domain.repository

import com.techhub.flatsplit.domain.model.UserAuth

interface AuthRepository {

    suspend fun signInWithGoogle(
        idToken: String
    ): Result<UserAuth>

    fun isUserLoggedIn(): Boolean

    suspend fun signOut()
}