package com.techhub.flatsplit.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.TextPrimary
import java.text.SimpleDateFormat

@Composable
fun ExpenseActivityCard(
    icon: ImageVector,
    category: String,
    name: String,
    timestamp: Timestamp,
    amountPaise: Long

) {

    val dimens = AppTheme.dimensions


    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(dimens.small),
        border = BorderStroke(width = 1.dp, color = BorderSubtle),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier.padding(dimens.large),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = category,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.width(dimens.large))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = "$name paid · ${formatTimestamp(timestamp)}"
                )
            }

            Text(
                text = "₹%,.2f".format(amountPaise / 100.0),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }

}


private fun formatTimestamp(
    timestamp: Timestamp
): String {

    val formatter = SimpleDateFormat(
        "dd MMM",
        Locale.getDefault()
    )

    return formatter.format(timestamp.toDate())
}

