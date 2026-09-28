package com.techhub.flatsplit.domain.repository

import com.techhub.flatsplit.domain.model.User

interface UserRepository {

    suspend fun getUser(userId: String): User?

    suspend fun getUsers(userIds: List<String>): List<User>

    suspend fun saveUser(user: User)
}