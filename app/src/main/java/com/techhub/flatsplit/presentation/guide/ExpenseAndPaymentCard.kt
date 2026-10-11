package com.techhub.flatsplit.presentation.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.NegativeRoseBg
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.PositiveTealBg
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
internal fun ExpenseExampleCallout() {
    val dimens = AppTheme.dimensions
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PositiveTealBg)
    ) {
        Column(modifier = Modifier.padding(dimens.large)) {
            Text(
                text = "EXAMPLE",
                style = MaterialTheme.typography.labelMedium,
                color = PositiveTeal,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spaced()
            Text(
                text = "If four flatmates share ₹1,200 equally, each person's share " +
                        "is ₹300. Your actual share depends on who is selected for the expense.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}

@Composable
internal fun NotTransferCallout() {
    val dimens = AppTheme.dimensions
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        colors = CardDefaults.cardColors(containerColor = NegativeRoseBg)
    ) {
        Column(modifier = Modifier.padding(dimens.large)) {
            Text(
                text = "IMPORTANT",
                style = MaterialTheme.typography.labelMedium,
                color = NegativeRose,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spaced()
            Text(
                text = "FlatSplit records the payment you report. It does not transfer " +
                        "money itself and does not verify that your bank or UPI transaction succeeded. " +
                        "Only report a payment after you have actually made it.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}

@Composable
internal fun PaymentFlowSection() {
    val dimens = AppTheme.dimensions
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        colors = CardDefaults.cardColors(containerColor = SurfaceRaised)
    ) {
        Column(modifier = Modifier.padding(dimens.large)) {
            Text(
                text = "A reported payment changes the balance only after the recipient " +
                        "confirms that the money was received.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(dimens.large))
            PaymentFlowStep(
                icon = Icons.Outlined.Schedule,
                color = AccentGold,
                title = "Pending",
                description = "The payer reported a payment. The recipient still needs " +
                        "to confirm receipt."
            )
            PaymentFlowConnector()
            PaymentFlowStep(
                icon = Icons.Outlined.CheckCircle,
                color = PositiveTeal,
                title = "Confirmed",
                description = "The recipient confirms receipt. The settlement can now " +
                        "affect the calculated balance."
            )
            PaymentFlowConnector()
            PaymentFlowStep(
                icon = Icons.Outlined.Cancel,
                color = NegativeRose,
                title = "Not received",
                description = "The recipient rejects the report. The payment does not " +
                        "reduce the outstanding balance."
            )
        }
    }
}

@Composable
private fun PaymentFlowStep(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun PaymentFlowConnector() {
    Box(
        modifier = Modifier
            .padding(start = 17.dp)
            .width(2.dp)
            .height(20.dp)
            .background(BorderSubtle)
    )
}
