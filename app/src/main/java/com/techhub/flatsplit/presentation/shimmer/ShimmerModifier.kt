package com.techhub.flatsplit.presentation.shimmer

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerEffect(): Modifier = composed {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(
        label = "shimmer"
    )

    val shimmerPosition by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPosition"
    )

    val width = size.width.toFloat()
    val height = size.height.toFloat()

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF91857E),
                Color(0xFF2B2420),
                Color(0xFFAB9386)
            ),
            start = Offset(x = width * shimmerPosition, y = 0f),
            end = Offset(x = width * (shimmerPosition + 1f), y = height)
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}