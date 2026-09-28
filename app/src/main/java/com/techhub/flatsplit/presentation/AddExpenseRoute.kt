package com.techhub.flatsplit.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.presentation.addexpenses.AddExpenseScreen
import com.techhub.flatsplit.presentation.addexpenses.AddExpenseViewModel
import com.techhub.flatsplit.presentation.components.ToastEvent

@Composable
fun AddExpenseRoute(
    room: FlatRoom,
    navController: NavHostController,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentUserId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid
            ?: return

    ToastEvent(
        event = viewModel.event
    )

    LaunchedEffect(room.id) {
        viewModel.loadMembers(room.memberIds)
    }

    if (uiState.isLoadingMembers) {
        //  loading UI
        return
    }

    AddExpenseScreen(
        members = uiState.members,
        currentUserId = currentUserId,
        isLoading = uiState.isLoading,
        errorMessage = uiState.error,

        onBack = {
            navController.popBackStack()
        },
        onAddExpense = { title,
                         amountPaise,
                         category,
                         paidBy,
                         splitBetween ->

            viewModel.addExpense(
                roomId = room.id,
                title = title,
                amountPaise = amountPaise,
                category = category,
                paidBy = paidBy,
                splitBetween = splitBetween
            )
        }

    )

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.clearSuccess()
            navController.popBackStack()
        }
    }
}