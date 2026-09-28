package com.techhub.flatsplit.domain.usecase.authusecase

import com.techhub.flatsplit.domain.model.UserAuth
import com.techhub.flatsplit.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        idToken: String
    ): Result<UserAuth>{

        return authRepository.signInWithGoogle(idToken = idToken)
    }

}