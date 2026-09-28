package com.techhub.flatsplit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark

@Composable
fun AppError(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val dimens = AppTheme.dimensions

    Column(
        modifier = modifier.fillMaxWidth()
            .background(BgDark),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(
            modifier = Modifier.height(dimens.medium)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )

        if (onRetry != null) {
            Spacer(
                modifier = Modifier.height(dimens.medium)
            )

            Button(
                onClick = onRetry
            ) {
                Text("Try Again")
            }
        }
    }
}