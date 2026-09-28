package com.techhub.flatsplit.presentation.flatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AccentCoral
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun FlatCustomCard(
    icon: ImageVector,
    title: String,
    tint: Color = LocalContentColor.current,
    subTitle: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val dimens = AppTheme.dimensions
    Box(
        modifier = Modifier.padding(start = dimens.medium)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(SurfaceDark)
                .widthIn(250.dp)
                .border(
                    width = 1.dp, shape = RoundedCornerShape(25.dp),
                    color = BorderSubtle
                )
                .fillMaxWidth()
                .padding(vertical = dimens.large, horizontal = dimens.large),
            verticalArrangement = Arrangement.SpaceBetween
        ){

            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = modifier,
                tint = tint
                )
            Spacer(modifier = Modifier.height(dimens.smallMedium))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(dimens.smallMedium))
            Text(
                text = subTitle,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Button(
                onClick = { onClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentCoral,
                    contentColor = TextPrimary
                )
            ) {

                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

        }
    }
}