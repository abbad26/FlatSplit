package com.techhub.flatsplit.presentation.flatroom

import com.techhub.flatsplit.domain.model.FlatRoom

data class FlatRoomUiState(
    val isLoading: Boolean = false,
    val rooms: List<FlatRoom> = emptyList(),
    val createdRoom: FlatRoom? = null,
    val joinSuccess: Boolean = false,
    val error: String? = null
)