package com.techhub.flatsplit.presentation.manageflat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.usecase.GetUsersUseCase
import com.techhub.flatsplit.domain.usecase.manageflatusecase.DeleteFlatUseCase
import com.techhub.flatsplit.domain.usecase.manageflatusecase.RemoveMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageFlatViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val removeMemberUseCase: RemoveMemberUseCase,
    private val deleteFlatUseCase: DeleteFlatUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageFlatUiState())
    val uiState: StateFlow<ManageFlatUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event: SharedFlow<String> = _event.asSharedFlow()


    fun loadRoom(room: FlatRoom) {

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    room = room,
                    error = null
                )
                val users = getUsersUseCase(room.memberIds)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    room = room,
                    members = users,
                    error = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Failed to load flat"
                )
            }
        }
    }


    fun removeMember(
        memberId: String
    ) {
        val room = _uiState.value.room
            ?: return

        viewModelScope.launch {
            try {

                _uiState.value =
                    _uiState.value.copy(
                        isActionLoading = true,
                        error = null
                    )

                removeMemberUseCase(
                    roomId = room.id,
                    userId = memberId
                )
                val updatedRoom =
                    room.copy(
                        memberIds = room.memberIds
                            .filterNot { it == memberId }
                    )
                val updatedMembers =
                    _uiState.value.members
                        .filterNot { it.id == memberId }

                _uiState.value =
                    _uiState.value.copy(
                        isActionLoading = false,
                        room = updatedRoom,
                        members = updatedMembers
                    )
                _event.emit(
                    "Member removed successfully"
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isActionLoading = false,
                        error = e.message
                            ?: "Failed to remove member"
                    )
            }
        }
    }


    fun deleteFlat() {

        val room = _uiState.value.room
            ?: return

        viewModelScope.launch {
            try {

                _uiState.value =
                    _uiState.value.copy(
                        isActionLoading = true,
                        error = null
                    )

                deleteFlatUseCase(room.id)

                _uiState.value =
                    _uiState.value.copy(
                        isActionLoading = false,
                        isDeleted = true
                    )

                _event.emit(
                    "Flat deleted successfully"
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        isActionLoading = false,
                        error = e.message
                            ?: "Failed to delete flat"
                    )
            }
        }
    }

    fun clearError() {
        _uiState.value =
            _uiState.value.copy(error = null)
    }

    fun clearDeleted() {
        _uiState.value =
            _uiState.value.copy(isDeleted = false)
    }
}


data class ManageFlatUiState(
    val isLoading: Boolean = false,
    val isActionLoading: Boolean = false,
    val room: FlatRoom? = null,
    val members: List<User> = emptyList(),
    val error: String? = null,
    val isDeleted: Boolean = false
)