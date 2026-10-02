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
    room: FlatRoom,
    navController: NavHostController,
    viewModel: ManageFlatViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

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

    LaunchedEffect(room.id) {
        viewModel.loadRoom(room)
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {

            viewModel.clearDeleted()

            navController.popBackStack()
        }
    }

    ManageFlatScreen(
        room = uiState.room ?: room,
        members = uiState.members,
        currentUserId = currentUserId,
        isActionLoading = uiState.isActionLoading,
        errorMessage = uiState.error,

        onBack = {
            navController.popBackStack()
        },

        onRemoveMember = { memberId ->
            viewModel.removeMember(memberId)
        },

        onDeleteFlat = {
            viewModel.deleteFlat()
        }
    )
}