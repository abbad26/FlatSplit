package com.techhub.flatsplit.presentation.components


import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


/*
SharedFlow → one-time UI events
StateFlow → screen state
 */
@Composable
fun ToastEvent(
    event: SharedFlow<String>
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        event.collectLatest { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}