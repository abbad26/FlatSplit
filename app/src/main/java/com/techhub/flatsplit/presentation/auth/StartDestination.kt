package com.techhub.flatsplit.presentation.auth

sealed interface StartDestination {

    data object Loading: StartDestination
    data object Login : StartDestination
    data object Home : StartDestination
}