package com.techhub.flatsplit.domain.usecase.upiusecase

import com.techhub.flatsplit.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUpiIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        upiId: String
    ) {
        repository.updateUpiId(
            userId = userId,
            upiId = upiId.trim()
        )
    }
}