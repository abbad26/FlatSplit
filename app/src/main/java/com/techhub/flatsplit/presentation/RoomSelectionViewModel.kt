package com.techhub.flatsplit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.usecase.GetUserRoomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomSelectionUiState(
    val isLoading: Boolean = true,
    val rooms: List<FlatRoom> = emptyList(),
    val selectedRoom: FlatRoom? = null,
    val error: String? = null
)

@HiltViewModel
class RoomSelectionViewModel @Inject constructor(
    private val getUserRoomsUseCase: GetUserRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomSelectionUiState())

    val uiState: StateFlow<RoomSelectionUiState> = _uiState.asStateFlow()

    fun loadRooms(userId: String) {

        viewModelScope.launch {

            try {
                getUserRoomsUseCase(userId).collect { rooms ->

                        val currentSelectedId =
                            _uiState.value.selectedRoom?.id

                        val selectedRoom =
                            rooms.firstOrNull {
                                it.id == currentSelectedId
                            } ?: rooms.firstOrNull()

                        _uiState.value =
                            RoomSelectionUiState(
                                isLoading = false,
                                rooms = rooms,
                                selectedRoom = selectedRoom
                            )
                    }

            } catch (e: Exception) {

                _uiState.value =
                    RoomSelectionUiState(
                        isLoading = false,
                        error = e.message
                            ?: "Failed to load flats"
                    )
            }
        }
    }

    fun selectRoom(room: FlatRoom) {
        _uiState.value =
            _uiState.value.copy(
                selectedRoom = room
            )
    }
}