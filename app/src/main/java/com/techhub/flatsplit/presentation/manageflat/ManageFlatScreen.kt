package com.techhub.flatsplit.presentation.manageflat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun ManageFlatScreen(
    room: FlatRoom,
    members: List<User>,
    currentUserId: String,
    isActionLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onRemoveMember: (String) -> Unit,
    onDeleteFlat: () -> Unit
) {

    val dimens = AppTheme.dimensions

    var memberToRemove by remember { mutableStateOf<User?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(dimens.large)
            .background(BgDark)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimens.large
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector =
                        Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Manage Flat",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = dimens.large,
                vertical = dimens.medium
            ),
            verticalArrangement =
                Arrangement.spacedBy(dimens.medium)
        ) {

            item {

                Text(
                    text = room.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(
                    modifier = Modifier.height(dimens.smallMedium)
                )

                Text(
                    text = "Invite code: ${room.inviteCode}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(dimens.medium)
                )

                Text(
                    text = "Members",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            items(
                items = members,
                key = { it.id }
            ) { member ->

                MemberRow(
                    member = member,
                    isOwner = member.id == room.createdBy,
                    isCurrentUser = member.id == currentUserId,
                    enabled = !isActionLoading,
                    onRemove = {
                        memberToRemove = member
                    }
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(dimens.large)
                )

                errorMessage?.let { message ->

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(dimens.medium)
                    )
                }

                Button(
                    onClick = {
                        showDeleteDialog = true
                    },
                    enabled = !isActionLoading &&
                            currentUserId == room.createdBy,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.DeleteOutline,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )

                    Text("Delete Flat")
                }
            }
        }
    }

    memberToRemove?.let { member ->

        AlertDialog(
            onDismissRequest = {
                memberToRemove = null
            },
            title = {
                Text("Remove ${member.name}?")
            },
            text = {
                Text(
                    "${member.name} will no longer be able " +
                            "to access this flat."
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        onRemoveMember(member.id)
                        memberToRemove = null
                    },
                    enabled = !isActionLoading
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        memberToRemove = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete ${room.name}?")
            },
            text = {
                Text(
                    "This will permanently delete the flat, " +
                            "expenses and settlement records."
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteFlat()
                    },
                    enabled = !isActionLoading
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
private fun MemberRow(
    member: User,
    isOwner: Boolean,
    isCurrentUser: Boolean,
    enabled: Boolean,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = null
        )

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = member.name,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Text(
                text = when {
                    isCurrentUser -> "You"
                    isOwner -> "Owner"
                    else -> member.email
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        if (!isOwner && !isCurrentUser) {
            TextButton(
                onClick = onRemove,
                enabled = enabled
            ) {
                Text("Remove")
            }
        }
    }
}