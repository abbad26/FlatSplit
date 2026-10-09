package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        userIds: List<String>
    ): List<User> {
        return repository.getUsers(userIds)
    }
}