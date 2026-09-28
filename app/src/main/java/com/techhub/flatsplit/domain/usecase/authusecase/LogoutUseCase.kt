package com.techhub.flatsplit.domain.usecase.authusecase

import com.techhub.flatsplit.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() {

        repository.signOut()
    }
}