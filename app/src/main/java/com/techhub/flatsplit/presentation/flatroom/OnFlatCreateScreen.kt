package com.techhub.flatsplit.presentation.flatroom

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.R
import com.techhub.flatsplit.ui.theme.AccentCoral
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AccentGoldDeep
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.PositiveTealBg
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun OnFlatCreateScreen(
    name: String,
    inviteCode: String,
    onBack: () -> Unit
){

    val dimens = AppTheme.dimensions

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.large)
            .background(color = BgDark)
    ) {

        Spacer(modifier = Modifier.height(dimens.large))
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = SurfaceRaised, shape = CircleShape
                )
                .clip(shape = CircleShape)
        ) {
            IconButton(
                onClick = { onBack()}

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = NegativeRose,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.large))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( dimens.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                    painter = painterResource(R.drawable.party_popper),
                    contentDescription = "flat is created",
                    modifier = Modifier.size(100.dp)
                )

            Spacer(modifier = Modifier.height(dimens.medium))
            Text(
                text = "Flat Room $name is Ready",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(dimens.medium))
            Text(
                text = "Share this code with your flatmates",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(dimens.large))

            InviteCodeCard(
                inviteCode = inviteCode
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(dimens.large),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(inviteCode))
                        scope.launch {
                            snackbarHostState.showSnackbar("Text copied to clipboard")
                        }
                    },
                    modifier = Modifier
                        .size(width = 140.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGoldDeep.copy(0.8f),
                        contentColor = TextSecondary
                    )
                ) {
                    Text(
                        text = "Copy Code",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, inviteCode)
                            putExtra(Intent.EXTRA_SUBJECT, "Invite code")
                            type = "text/plain"
                        }
                        val chooserIntent = Intent.createChooser(sendIntent, "Share via")
                        context.startActivity(chooserIntent)
                    },
                    modifier = Modifier
                        .size(width = 140.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PositiveTealBg.copy(0.8f),
                        contentColor = TextSecondary
                    )
                ) {
                    Text(
                        text = "Share Invite",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }


            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PositiveTealBg),

            )


        }
    }
}


@Composable
private fun InviteCodeCard(
    inviteCode: String
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceRaised
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = BorderSubtle),

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "INVITE CODE",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(AppTheme.dimensions.medium))

            Text(
                text = inviteCode,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }
    }

}