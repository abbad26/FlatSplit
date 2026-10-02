package com.techhub.flatsplit.presentation.settleup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.model.settlement.SettlementSuggestion
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun SettlementSuggestionCard(
    suggestion: SettlementSuggestion,
    fromName: String,
    toName: String,
    currentUserId: String,
    pendingSettlement: Settlement?,
    onSettle: () -> Unit,
    onConfirm: () -> Unit
) {

    val dimens = AppTheme.dimensions

    val isUserDebtor = suggestion.fromUserId == currentUserId
    val isUserCreditor = suggestion.toUserId == currentUserId

    val hasPendingSettlement = pendingSettlement != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = BorderSubtle)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.large)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                InitialNameCircle(
                    name = fromName,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(
                    modifier = Modifier.width(dimens.large)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "$fromName → $toName",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.size(4.dp)
                    )

                    Text(
                        text = formatMoney(suggestion.amount),
                        color = TextSecondary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                InitialNameCircle(
                    name = toName,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(
                modifier = Modifier.size(dimens.medium)
            )

            when {

                // You owe the money and haven't requested settlement yet
                isUserDebtor && !hasPendingSettlement -> {

                    Button(
                        onClick = onSettle,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = buttonColors(
                            containerColor = AccentGold
                        )
                    ) {
                        Text("Settle")
                    }
                }

                // You owe the money and are waiting for confirmation
                isUserDebtor && hasPendingSettlement -> {

                    Text(
                        text = "Waiting for confirmation",
                        modifier = Modifier.fillMaxWidth(),
                        color = NegativeRose,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // You are the creditor and need to confirm
                isUserCreditor && hasPendingSettlement -> {

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = buttonColors(
                            containerColor = AccentGold
                        )
                    ) {
                        Text("Confirm Payment")
                    }
                }
            }
        }
    }
}

private fun formatMoney(amountPaise: Long): String {
    return if (amountPaise % 100 == 0L) {
        "₹%,d".format(amountPaise / 100)
    } else {
        "₹%,.2f".format(amountPaise / 100.0)
    }
}

@Composable
private fun InitialNameCircle(
    name: String,
    modifier: Modifier = Modifier
) {

    val initialName = name
        .firstOrNull()
        ?.uppercase()
        ?: "?"

    Box(
        modifier = modifier
            .background(
                color = avatarColor(name),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = initialName,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

private val avatarColors = listOf(
    Color(0xFF514477),
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