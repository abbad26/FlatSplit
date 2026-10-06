package com.techhub.flatsplit.presentation.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.BgDark

@Composable
fun FeedbackScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(BgDark),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Coming soon...",
            fontWeight = FontWeight.Bold
        )
    }
}