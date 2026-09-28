package com.techhub.flatsplit.domain.usecase.authusecase

import com.techhub.flatsplit.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}