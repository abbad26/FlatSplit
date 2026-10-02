package com.techhub.flatsplit.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.usecase.SaveUserUseCase
import com.techhub.flatsplit.domain.usecase.authusecase.CheckAuthUseCase
import com.techhub.flatsplit.domain.usecase.authusecase.GoogleSignInUseCase
import com.techhub.flatsplit.domain.usecase.authusecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val checkAuthUseCase: CheckAuthUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(
        LoginUiState.Idle
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    private val _startDestination = MutableStateFlow<StartDestination>(
        StartDestination.Loading
    )

    val startDestination = _startDestination.asStateFlow()

    init {
        checkAuthentication()
    }


    private fun checkAuthentication() {
        viewModelScope.launch {

            val isLoggedIn = checkAuthUseCase()

            _startDestination.value =
                if (isLoggedIn) {
                    StartDestination.Home
                } else {
                    StartDestination.Login
                }
        }
    }
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {

            _uiState.value = LoginUiState.Loading

            val result = googleSignInUseCase(idToken)

            result
                .onSuccess { userAuth ->

                    try {
                        val user = User(
                            id = userAuth.uid,
                            name = userAuth.name,
                            email = userAuth.email,
                            photoUrl = userAuth.profileImage
                        )

                        saveUserUseCase(user)

                        _uiState.value = LoginUiState.Success(userAuth)

                        _startDestination.value = StartDestination.Home
                    } catch (e: Exception){

                        _uiState.value = LoginUiState.Error(
                            e.message ?: "Failed to save user"
                        )
                    }
                }

                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(
                        error.message ?: "Google Sign-In failed"
                    )
                }

        }

    }

    fun logout(){
        viewModelScope.launch {

            try {
                logoutUseCase()

                _event.emit("Logged out successfully")
                _uiState.value = LoginUiState.Idle
                _startDestination.value = StartDestination.Login
            }catch (e: Exception){

                _event.emit(e.message ?: "Logout failed")
            }

        }
    }

    fun setError(message: String) {

        _uiState.value =
            LoginUiState.Error(message)
    }
}