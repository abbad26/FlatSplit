package com.techhub.flatsplit.presentation.flatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.presentation.components.CustomDialog
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import java.util.UUID

@Composable
fun FlatsScreen(
    onFlatCreated: (FlatRoom) -> Unit,
    onFlatJoined: () -> Unit
) {

    val viewModel: FlatRoomViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val dimens = AppTheme.dimensions
    var showCreateDialog by remember { mutableStateOf(false) }
    var showJoinDialog by remember { mutableStateOf(false) }
    var flatName by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }


    LaunchedEffect(uiState.createdRoom) {
        uiState.createdRoom?.let { room ->
            onFlatCreated(room)

            viewModel.clearCreatedRoom()
        }
    }

    LaunchedEffect(uiState.joinSuccess) {
        if (uiState.joinSuccess) {
            showJoinDialog = false
            inviteCode = ""

            viewModel.clearJoinSuccess()
            onFlatJoined()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(BgDark)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.large),
            verticalArrangement = Arrangement.spacedBy(dimens.large)
        ) {

            Text(
                text = "Flats",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 10.dp)
            )

            FlatCustomCard(
                icon = Icons.Outlined.AddHome,
                tint = AccentGold,
                title = "Create a Flat",
                subTitle = "Create a shared space for your roommates",
                buttonText = "Create Flat",
                onClick = {
                    showCreateDialog = true
                }
            )

            FlatCustomCard(
                icon = Icons.Outlined.GroupAdd,
                tint = AccentGold,
                title = "Join a Flat",
                subTitle = "Enter an invite code from your roommate",
                buttonText = "Join Flat",
                onClick = {
                    showJoinDialog = true
                }
            )


        }
    }

    if (showCreateDialog){
        CustomDialog(
            title = "Create a Flat",
            placeholder = "Flat name",
            confirmText = "Create",
            value = flatName,
            onValueChange = { flatName = it},
            onDismiss = {
                showCreateDialog = false
            },
            onConfirm = {
                if (flatName.isNotBlank()){
                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

                    if (currentUserId != null){
                        val room = FlatRoom(
                            id = UUID.randomUUID().toString(),
                            name = flatName.trim(),
                            inviteCode = generateInviteCode(),
                            createdBy = currentUserId,
                            memberIds = listOf(currentUserId)
                        )
                        viewModel.createRoom(room)

                        showCreateDialog = false
                        flatName = ""
                    }


                }
            }
        )


    }
    if (showJoinDialog) {
        CustomDialog(
            title = "Join a Flat",
            placeholder = "Invite code",
            confirmText = "Join",
            value = inviteCode,
            onValueChange = { inviteCode = it },
            onDismiss = {
                showJoinDialog = false
            },
            onConfirm = {
                if (inviteCode.isNotBlank()) {

                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

                    if (currentUserId != null){
                        viewModel.joinRoom(
                            userId = currentUserId,
                            inviteCode = inviteCode
                        )
                    }
                }
            }
        )
    }
}

private fun generateInviteCode(): String {

    return UUID.randomUUID()
        .toString()
        .replace("-", "")
        .take(6)
        .uppercase()
}