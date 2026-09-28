package com.techhub.flatsplit.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun BalanceSummaryCard(
    totalSpent: Long,
    yourPaid: Long,
    youOwe: Long,
    youGet: Long,
    memberCount: Int
) {

    val dimens = AppTheme.dimensions

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceDark,
        border = BorderStroke(
            width = 1.dp,
            color = BorderSubtle
        ),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(25.dp),
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = dimens.large,
                vertical = dimens.large
            )
        ) {

            Text(
                text = "Total Spends",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(
                modifier = Modifier.height(dimens.medium)
            )

            Text(
                text = "₹%.2f".format(totalSpent / 100.0),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            Spacer(
                modifier = Modifier.height(dimens.medium)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                BalanceValue(
                    title = "Your Paid",
                    value = "₹%.2f".format(yourPaid / 100.0)
                )

                BalanceValue(
                    title = "You Owe",
                    value = "₹%.2f".format(youOwe / 100.0),
                    valueColor = Color(0xFFFF6B6B)
                )
            }

            Spacer(
                modifier = Modifier.height(dimens.large)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                BalanceValue(
                    title = "You Get",
                    value = "₹%.2f".format(youGet / 100.0),
                    valueColor = Color(0xFF2ECC9B)
                )

                BalanceValue(
                    title = "Members",
                    value = memberCount.toString()
                )
            }
        }
    }
}


@Composable
private fun BalanceValue(title: String, value: String, valueColor: Color = TextPrimary) {
    Column {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = valueColor)
    }
}