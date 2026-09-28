package com.techhub.flatsplit.presentation.auth

import com.techhub.flatsplit.domain.model.UserAuth

sealed interface LoginUiState {

    data object Idle: LoginUiState

    data object Loading: LoginUiState

    data class Success(
        val user: UserAuth
    ): LoginUiState

    data class Error(
        val message: String
    ): LoginUiState
}