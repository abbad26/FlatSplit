package com.techhub.flatsplit.presentation.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.usecase.upiusecase.GetUserUseCase
import com.techhub.flatsplit.domain.usecase.upiusecase.UpdateUpiIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUpiIdUseCase: UpdateUpiIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val user = getUserUseCase(userId)

                if (user == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User profile not found."
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    upiId = user.upiId,
                    isLoading = false,
                    error = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Failed to load profile."
                )
            }
        }
    }

    fun updateUpiId(
        userId: String,
        enteredUpiId: String
    ) {
        val upiId = enteredUpiId.trim()

        if (_uiState.value.isSaving) return

        if (!isValidUpiId(upiId)) {
            _uiState.value = _uiState.value.copy(
                error = "Enter a valid UPI ID, such as name@bank.",
                successMessage = null
            )
            return }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                successMessage = null
            )

            try {
                updateUpiIdUseCase(
                    userId = userId,
                    upiId = upiId
                )

                _uiState.value = _uiState.value.copy(
                    upiId = upiId,
                    isSaving = false,
                    error = null,
                    successMessage = "UPI ID saved successfully."
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message
                        ?: "Failed to save UPI ID.",
                    successMessage = null
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }

    fun onUpiIdChange(value: String) {
        _uiState.value = _uiState.value.copy(
            upiId = value,
            error = null,
            successMessage = null
        )
    }

    private fun isValidUpiId(upiId: String): Boolean {
        return Regex(
            "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+$"
        ).matches(upiId)
    }

}