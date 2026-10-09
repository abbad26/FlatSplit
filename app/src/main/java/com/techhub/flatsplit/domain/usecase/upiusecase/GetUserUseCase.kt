package com.techhub.flatsplit.domain.usecase.upiusecase

import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        userId: String
    ): User? {
        return repository.getUser(userId)
    }
}