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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRoseBg
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary



@Composable
fun ProfileScreen(
    onManageFlat: () -> Unit,
    onLogout: () -> Unit
) {

    val user = FirebaseAuth.getInstance().currentUser
    val dimens = AppTheme.dimensions

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

            ManageProfileCard(
                onManageFlat = {
                    onManageFlat()
                },
                onFeedback = {}

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