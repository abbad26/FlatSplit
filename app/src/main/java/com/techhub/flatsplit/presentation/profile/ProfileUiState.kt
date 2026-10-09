package com.techhub.flatsplit.presentation.profile

data class ProfileUiState(
    val upiId: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)