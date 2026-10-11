package com.techhub.flatsplit.presentation.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
internal fun BalanceExplainerCard() {
    val dimens = AppTheme.dimensions

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        colors = CardDefaults.cardColors(containerColor = SurfaceRaised)
    ) {
        Column(modifier = Modifier.padding(dimens.large)) {
            Text(
                text = "Settle Up shows the overall balance for your selected flat:",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spaced()
            BalanceLegendRow(
                label = "YOU OWE OVERALL",
                color = NegativeRose,
                description = "You need to pay this amount to one or more flatmates."
            )
            Spaced()
            BalanceLegendRow(
                label = "YOU WILL RECEIVE",
                color = PositiveTeal,
                description = "Flatmates collectively owe you this amount."
            )
            Spaced()
            BalanceLegendRow(
                label = "ALL SETTLED",
                color = TextSecondary,
                description = "No outstanding amount remains right now."
            )
            Spaced()
            Text(
                text = "Home shows a flat breakdown with Total Spends, Total Settled, " +
                        "Your Paid, You Owe, You Get, and member count.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun BalanceLegendRow(
    label: String,
    color: Color,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
