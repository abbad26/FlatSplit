package com.techhub.flatsplit.presentation.manageflat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.presentation.components.ToastEvent

@Composable
fun ManageFlatRoute(
    room: FlatRoom?,
    navController: NavHostController,
    viewModel: ManageFlatViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentUserId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid

    if (currentUserId == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    ToastEvent(
        event = viewModel.event
    )

    // Load data only when flat exists
    LaunchedEffect(room?.id, currentUserId) {
        room?.let {
            viewModel.loadRoom(
                room = it,
                currentUserId = currentUserId
            )
        }
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            viewModel.clearDeleted()
            navController.popBackStack()
        }
    }

    // No flat
    if (room == null) {
        EmptyManageFlatScreen(
            onBack = {
                navController.popBackStack()
            }
        )
        return
    }

    // Flat exists
    ManageFlatScreen(
        room = uiState.room ?: room,
        members = uiState.members,
        currentUserId = currentUserId,
        currentUserBalance = uiState.currentBalance,
        isActionLoading = uiState.isActionLoading,
        isLoading = uiState.isLoading,
        errorMessage = uiState.error,
        onBack = {
            navController.popBackStack()
        },
        onRemoveMember = { memberId ->
            viewModel.removeMember(memberId)
        },
        onLeaveFlatBlocked = {
            viewModel.leaveFlatBlocked()
        },
        onLeaveFlat = {
            viewModel.removeMember(currentUserId)
        },
        onDeleteFlat = {
            viewModel.deleteFlat()
        }
    )
}