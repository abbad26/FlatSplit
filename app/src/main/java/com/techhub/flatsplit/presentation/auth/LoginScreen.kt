package com.techhub.flatsplit.presentation.auth

import android.app.Activity
import android.content.Context
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.techhub.flatsplit.R
import com.techhub.flatsplit.presentation.components.PulseAnimation
import com.techhub.flatsplit.presentation.components.TripleOrbitLoadingAnimation
import com.techhub.flatsplit.ui.theme.AccentCoralDeep
import com.techhub.flatsplit.ui.theme.AccentGoldDeep
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.BorderDefault
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRoseBg
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.PositiveTealBg
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val credentialManager = remember(context) {
        CredentialManager.create(context)
    }

    val onGoogleSignInClick = {

        val activity = context.findActivity()

        if (activity != null) {

            coroutineScope.launch {

                executeGoogleSignIn(
                    activity = activity,
                    credentialManager = credentialManager,

                    onSuccess = { idToken ->
                        viewModel.signInWithGoogle(idToken)
                    },

                    onError = { exception ->
                        viewModel.setError(
                            exception.message
                                ?: "Google Sign-In failed"
                        )
                    }
                )
            }
        }
    }

    val dimensions = AppTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(dimensions.large)
        ) {


        when (val state = uiState) {

            LoginUiState.Idle -> {

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 80.dp),
                    horizontalAlignment = Alignment.Start,

                    ) {
                    Spacer(modifier = Modifier.height(dimensions.large))

                    LoginHeader()

                    Spacer(modifier = Modifier.height(dimensions.medium))

                    AuthOption(
                        image = R.drawable.google,
                        contentDescription = "Sign in with Google",
                        onClick = onGoogleSignInClick
                    )
                }


            }

            LoginUiState.Loading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                {
                    TripleOrbitLoadingAnimation()

                }

            }

            is LoginUiState.Success -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()

                }
            }

            is LoginUiState.Error -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = state.message,
                        color = TextSecondary
                    )

                    Spacer(
                        modifier = Modifier.height(dimensions.mediumLarge)
                    )

                    Button(
                        onClick = onGoogleSignInClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensions.large),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NegativeRoseBg.copy(0.8f)
                        )
                    ) {
                        Text(
                            text = "Try Again",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

private suspend fun executeGoogleSignIn(
    activity: Activity,
    credentialManager: CredentialManager,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {

    try {

        // Generate a random nonce
        val rawNonce = generateNonce()

        // Google Sign-In configuration
        val googleIdOption =
            GetGoogleIdOption.Builder()

                .setFilterByAuthorizedAccounts(false)

                .setServerClientId(
                    activity.getString(
                        R.string.web_client_id
                    )
                )

                .setAutoSelectEnabled(false)

                .setNonce(
                    hashNonce(rawNonce)
                )

                .build()

        // Create credential request
        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(
                    googleIdOption
                )
                .build()

        // Ask Credential Manager
        val result =
            credentialManager.getCredential(
                context = activity,
                request = request
            )

        // Get returned credential
        val credential = result.credential

        // Check Google credential type
        if (
            credential.type ==
            GoogleIdTokenCredential
                .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            // Convert credential
            val googleCredential =
                GoogleIdTokenCredential.createFrom(
                    credential.data
                )

            // Get ID token
            val idToken =
                googleCredential.idToken

            // Send token to ViewModel
            onSuccess(idToken)

        } else {

            onError(
                IllegalStateException(
                    "Unexpected credential type"
                )
            )
        }

    } catch (e: Exception) {

        onError(e)

//    } catch (e: GoogleIdTokenParsingException) {
//
//        onError(e)
    }
}

private fun generateNonce(): String {

    val randomBytes = ByteArray(32)

    SecureRandom().nextBytes(randomBytes)

    return Base64.encodeToString(
        randomBytes,
        Base64.NO_WRAP or Base64.URL_SAFE
    )
}

private fun hashNonce(
    nonce: String
): String {

    val bytes =
        MessageDigest
            .getInstance("SHA-256")
            .digest(
                nonce.toByteArray()
            )

    return bytes.joinToString("") {
        "%02x".format(it)
    }
}

private tailrec fun Context.findActivity(): Activity? {

    return when (this) {

        is Activity -> this

        is android.content.ContextWrapper ->
            baseContext.findActivity()

        else -> null
    }
}

@Composable
fun AuthOption(
    modifier: Modifier = Modifier,
    image: Int,
    tint: Color? = null,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val dimensions = AppTheme.dimensions

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SurfaceRaised.copy(0.3f), shape = RoundedCornerShape(dimensions.large))
            .border(
                width = 1.dp,
                color = BorderSubtle,
                shape = RoundedCornerShape(dimensions.large)
            )
            .clip(
                RoundedCornerShape(dimensions.large)
            )
            .clickable { onClick() }
            .padding(
                horizontal = dimensions.large * 2,
                vertical = dimensions.mediumLarge
            ),

        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val iconSize = dimensions.large * 2

            if (tint != null) {
                Icon(
                    painter = painterResource(image),
                    contentDescription = contentDescription,
                    tint = tint,
                    modifier = Modifier.size(iconSize)
                )
            } else {
                Image(
                    painter = painterResource(image),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }

    }
}

@Composable
fun LoginHeader(){
    val dimens = AppTheme.dimensions
    Column(
        modifier = Modifier.padding(dimens.medium),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(dimens.large))
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(color = SurfaceRaised.copy(0.5f), shape = RoundedCornerShape(25.dp))
                .border(width = 2.dp, shape = RoundedCornerShape(25.dp), color = BorderSubtle),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = null,
                tint = AccentGoldDeep,
                modifier = Modifier.size(45.dp)
            )
        }

        Spacer(modifier = Modifier.height(dimens.large))

        Text(
            text = "FlatSplit",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(dimens.large))

        Text(
            text = "Manage your flat expenses together — see who",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Normal,
            color = TextSecondary,
            fontSize = 16.sp
        )
        Text(
            text = "paid, who owes, and settle up in seconds.",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Normal,
            color = TextSecondary,
            fontSize = 16.sp
        )

    }
}
