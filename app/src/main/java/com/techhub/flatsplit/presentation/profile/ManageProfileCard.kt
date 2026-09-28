package com.techhub.flatsplit.presentation.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AccentGoldDeep
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary


@Composable
fun ManageProfileCard(
    onManageFlat: () -> Unit,
    onFeedback: () -> Unit
) {
    val dimens = AppTheme.dimensions

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderSubtle, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceRaised.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Group,
                        contentDescription = "Manage Flat",
                        tint = AccentGoldDeep
                    )

                    Spacer(
                        modifier = Modifier.width(22.dp)
                    )

                    Text(
                        text = "Manage Flat",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = onManageFlat
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = PositiveTeal.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(dimens.large)
            )

            HorizontalDivider(
                color = BorderSubtle
            )

            Spacer(
                modifier = Modifier.height(dimens.large)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Feedback,
                        contentDescription = "Drop your Feedback",
                        tint = AccentGoldDeep
                    )

                    Spacer(
                        modifier = Modifier.width(22.dp)
                    )

                    Text(
                        text = "Drop your Feedback",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = onFeedback
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = PositiveTeal.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(dimens.large)
            )
        }
    }
}
