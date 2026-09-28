package com.techhub.flatsplit.presentation.settleup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import kotlin.math.cos
import kotlin.math.sin



 @Composable
 fun BalanceSummaryCard(balance: Long){

     val isOwing = balance < 0
     val isReceiving = balance > 0

     val title = when{
         isOwing -> "YOU OWE OVERALL"
         isReceiving -> "YOU WILL RECEIVE"
         else -> "ALL SETTLED"
     }

     val amount = kotlin.math.abs(balance)

     val cardColor = if (isOwing){
         Color(0xFF3B1521)
     } else{
         Color(0xFF12362F)
     }

     val textColor = if (isOwing) {
         Color(0xFFFF5C7A)
     } else {
         Color(0xFF2DD4BF)
     }

     Box(
         modifier = Modifier
             .fillMaxWidth()
             .background(
                 color = cardColor,
                 shape = RoundedCornerShape(25.dp)
             )
             .border(width = 1.dp, color = BorderSubtle, shape = RoundedCornerShape(25.dp))
             .padding(
                 horizontal = AppTheme.dimensions.large,
                 vertical = AppTheme.dimensions.large
                 )
     ) {
         Column(
             modifier = Modifier.fillMaxWidth(),
             horizontalAlignment = Alignment.CenterHorizontally
         ) {
             Text(
                 text = title,
                 color = textColor,
                 style = MaterialTheme.typography.titleMedium,
                 fontWeight = FontWeight.Bold
             )
             Text(
                 text = "₹%,d".format(amount / 100),
                 modifier = Modifier.padding(
                     top = AppTheme.dimensions.medium
                 ),
                 color = textColor,
                 style = MaterialTheme.typography.headlineLarge,
                 fontWeight = FontWeight.Bold
             )

             BalanceGauge(
                 isOwing = isOwing,
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(150.dp)
             )
         }
     }
 }
@Composable
private fun BalanceGauge(
    isOwing: Boolean,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val strokeWidth = 20.dp.toPx()

        val center = Offset(
            x = size.width / 2,
            y = size.height * 0.92f
        )

        val radius = size.width * 0.31f

        val arcRect = Rect(
            left = center.x - radius,
            top = center.y - radius,
            right = center.x + radius,
            bottom = center.y + radius
        )

        // Negative / owe
        drawArc(
            color = Color(0xFFC99583),
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        // Positive / receive
        drawArc(
            color = Color(0xFF91AA9F),
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        // Top center marker
        drawCircle(
            color = Color(0xFF7E9084),
            radius = 10.dp.toPx(),
            center = Offset(
                center.x,
                center.y - radius
            )
        )

        val angle = if (isOwing) {
            Math.toRadians(94.0)
        } else {
            Math.toRadians(42.0)
        }

        val needleLength = radius * 0.8f

        val end = Offset(
            x = center.x + cos(angle).toFloat() * needleLength,
            y = center.y - sin(angle).toFloat() * needleLength
        )

        val needleColor = if (isOwing) {
            Color(0xFFB66A48)
        } else {
            Color(0xFF4D8065)
        }

        drawLine(
            color = needleColor,
            start = center,
            end = end,
            strokeWidth = 7.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawCircle(
            color = needleColor,
            radius = 12.dp.toPx(),
            center = center
        )
    }
}



