package com.techhub.flatsplit.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRoseBg
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary



@Composable
fun ProfileScreen(
    onGuideScreen: () -> Unit,
    onManageFlat: () -> Unit,
    onFeedback: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val user = FirebaseAuth.getInstance().currentUser
    val dimens = AppTheme.dimensions


    val uiState by viewModel.uiState.collectAsState()

    var isEditingUpiId by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            isEditingUpiId = false
        }
    }

    LaunchedEffect(user?.uid) {
        user?.uid?.let { userId ->
            viewModel.loadProfile(userId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileAvatar(
                    photoUrl = user?.photoUrl?.toString(),
                    displayName = user?.displayName
                )

                Spacer(modifier = Modifier.width(dimens.large))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = user?.displayName ?: "User",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(dimens.smallMedium))

                    Text(
                        text = user?.email ?: "",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)


            Spacer(modifier = Modifier.height(dimens.large))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
//                Text(
//                    text = "Payment Details",
//                    color = TextPrimary,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(dimens.smallMedium))
//
//                Text(
//                    text = "Add your UPI ID to receive payments from flatmates.",
//                    color = TextSecondary,
//                    style = MaterialTheme.typography.bodyMedium
//                )

                Spacer(modifier = Modifier.height(dimens.medium))

                when {
                    // Show a loader while retrieving the saved UPI ID.
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }

                    // Show the saved UPI ID with an Edit button.
//                    uiState.upiId.isNotBlank() && !isEditingUpiId -> {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Column(
//                                modifier = Modifier.weight(1f)
//                            ) {
//                                Text(
//                                    text = "Your UPI ID",
//                                    color = TextSecondary,
//                                    style = MaterialTheme.typography.labelMedium
//                                )
//
//                                Spacer(
//                                    modifier = Modifier.height(4.dp)
//                                )
//
//                                Text(
//                                    text = uiState.upiId,
//                                    color = TextPrimary,
//                                    style = MaterialTheme.typography.titleMedium,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            }
//
//                            TextButton(
//                                onClick = {
//                                    viewModel.clearMessages()
//                                    isEditingUpiId = true
//                                }
//                            ) {
//                                Text(
//                                    text = "Edit",
//                                    color = PositiveTeal
//                                    )
//                            }
//                        }
//                    }

                    // Show the input form for a new UPI ID or when editing.
//                    else -> {
//                        OutlinedTextField(
//                            value = uiState.upiId,
//                            onValueChange = viewModel::onUpiIdChange,
//                            modifier = Modifier.fillMaxWidth(),
//                            label = {
//                                Text("UPI ID")
//                            },
//                            placeholder = {
//                                Text("yourname@bank")
//                            },
//                            singleLine = true,
//                            enabled = !uiState.isSaving
//                        )
//
//                        uiState.error?.let { error ->
//                            Spacer(
//                                modifier = Modifier.height(4.dp)
//                            )
//
//                            Text(
//                                text = error,
//                                color = MaterialTheme.colorScheme.error,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//
//                        uiState.successMessage?.let { message ->
//                            Spacer(
//                                modifier = Modifier.height(4.dp)
//                            )
//
//                            Text(
//                                text = message,
//                                color = MaterialTheme.colorScheme.primary,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//
//                        Spacer(
//                            modifier = Modifier.height(dimens.medium)
//                        )
//
//                        Button(
//                            onClick = {
//                                user?.uid?.let { userId ->
//                                    viewModel.updateUpiId(
//                                        userId = userId,
//                                        enteredUpiId = uiState.upiId
//                                    )
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            enabled = user?.uid != null &&
//                                    !uiState.isLoading &&
//                                    !uiState.isSaving,
//                            shape = RoundedCornerShape(18.dp)
//                        ) {
//                            if (uiState.isSaving) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.size(20.dp),
//                                    strokeWidth = 2.dp,
//                                    color = MaterialTheme.colorScheme.onPrimary
//                                )
//                            } else {
//                                Text("Save UPI ID")
//                            }
//                        }
//                    }
                }
            }
            Spacer(modifier = Modifier.height(dimens.large))

            ManageProfileCard(
                onGuideScreen = { onGuideScreen()},
                onManageFlat = {
                    onManageFlat()
                },
                onFeedback = { onFeedback()}

            )
            Spacer(modifier = Modifier.height(dimens.large))

            Button(
                onClick = { onLogout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NegativeRoseBg,
                    contentColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    photoUrl: String?,
    displayName: String?
) {

    Box(
        modifier = Modifier
            .size(108.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = BorderSubtle,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        if (!photoUrl.isNullOrBlank()) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(avatarColor((displayName?.firstOrNull() ?: 'U').uppercase())),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (displayName?.firstOrNull() ?: 'U').uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


private val avatarColors = listOf(
    Color(0xFF5F43B2),
    Color(0xFF76A08C),
    Color(0xFFA9854A),
    Color(0xFFC58D73),
    Color(0xFF6F8FA3),
    Color(0xFF8F7193)
)

private fun avatarColor(name: String): Color {
    val index =
        kotlin.math.abs(name.hashCode()) % avatarColors.size

    return avatarColors[index]
}