package com.techhub.flatsplit.presentation.flatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.usecase.CreateRoomUseCase
import com.techhub.flatsplit.domain.usecase.GetUserRoomsUseCase
import com.techhub.flatsplit.domain.usecase.JoinRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class FlatRoomViewModel @Inject constructor(
    private val createRoomUseCase: CreateRoomUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val getUserRoomsUseCase: GetUserRoomsUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(FlatRoomUiState())
    val uiState: StateFlow<FlatRoomUiState> = _uiState.asStateFlow()

    fun loadUserRooms(userId: String){

        viewModelScope.launch {
            try {
                getUserRoomsUseCase(userId = userId)
                    .collect { rooms ->

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            rooms = rooms,
                            error = null
                        )
                    }
            } catch (e: Exception){

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun createRoom(room: FlatRoom){

        viewModelScope.launch {

            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                createRoomUseCase(room)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    createdRoom = room
                )

            } catch (e: CancellationException) {

                throw e

            }
            catch (e: Exception){

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to create room"
                    )
            }
        }
    }

    fun joinRoom(userId: String, inviteCode: String){

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null,
                    joinSuccess = false
                )

                joinRoomUseCase(
                    userId = userId,
                    inviteCode = inviteCode.trim().uppercase()
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    joinSuccess = true
                )

            } catch (e: Exception){

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to join room",
                    joinSuccess = false
                    )
            }
        }
    }

    fun clearError(){
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearCreatedRoom() {
        _uiState.value = _uiState.value.copy(
            createdRoom = null
        )
    }

    fun clearJoinSuccess() {
        _uiState.value = _uiState.value.copy(
            joinSuccess = false
        )
    }

}