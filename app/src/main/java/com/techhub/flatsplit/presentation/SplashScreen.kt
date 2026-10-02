package com.techhub.flatsplit.presentation

import android.window.SplashScreen
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techhub.flatsplit.R
import com.techhub.flatsplit.ui.theme.AccentCoral
import com.techhub.flatsplit.ui.theme.AccentCoralDeep
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class OrbitDot(
    val label: String,
    val color: Color,
    val offset: Offset
)

@Composable
fun SplashScreen(
    onAnimationFinished: () -> Unit
){

    val badgeScale = remember { Animatable(0.5f) }

    val dots = remember {
        listOf(
            OrbitDot(
                label = "A",
                color = AccentGold,
                offset = Offset(70f, 0f)
            ),OrbitDot(
                label = "A",
                color = PositiveTeal,
                offset = Offset(0f, -70f)
            ),OrbitDot(
                label = "S",
                color = NegativeRose,
                offset = Offset(0f, 70f)
            ),OrbitDot(
                label = "H",
                color = AccentCoralDeep,
                offset = Offset(-70f, 0f)
            )
        )
    }

    val dotScale = remember {
        dots.map{ Animatable(0.3f) }
    }

    val wordmarkAlpha = remember { Animatable(0f) }

    val wordmarkOffset = remember { Animatable(10f) }

    val taglineAlpha = remember { Animatable(0f) }

    val taglineOffset = remember { Animatable(10f) }

    val loaderAlpha = remember { Animatable(0f) }


    LaunchedEffect(Unit) {

        // Badge pops in
        badgeScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = 280f
            )
        )

        // Dots enter one by one
        dots.indices.forEach { index ->

            launch {

                delay(130L + index * 130L)

                dotScale[index].animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = 300f
                    )
                )
            }
        }

        // Wordmark
        delay(750L)

        launch {
            wordmarkAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(400)
            )
        }

        wordmarkOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(400)
        )

        // Tagline
        delay(120L)

        launch {
            taglineAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(400)
            )
        }

        taglineOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(400)
        )

        // Loader
        delay(130L)

        loaderAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(400)
        )

        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {

        DotGridBackground(
            dotColor = SurfaceRaised
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ){

                // Orbit ring
                Canvas(modifier = Modifier.size(140.dp)) {

                    drawCircle(
                        color = BorderSubtle,
                        radius = size.minDimension / 2,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                // house badge
                Box(
                    modifier = Modifier.size(84.dp)
                        .graphicsLayer{
                            scaleX = badgeScale.value
                            scaleY = badgeScale.value
                        }
                        .clip(RoundedCornerShape(26.dp))
                        .background(AccentCoral),
                    contentAlignment = Alignment.Center
                ){

                    Icon(
                        painter = painterResource(R.drawable.ic_splash_house),
                        contentDescription = null,
                        modifier = Modifier.size(38.dp)
                    )
                }

                // Orbit dots

                dots.forEachIndexed { index, dot ->

                    Box(
                        modifier = Modifier
                            .offset{
                                IntOffset(
                                    dot.offset.x.dp.roundToPx(),
                                    dot.offset.y.dp.roundToPx()
                                )
                            }
                            .size(30.dp)
                            .scale(dotScale[index].value)
                            .clip(CircleShape)
                            .background(dot.color),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = dot.label,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1B1310)
                        )
                    }
                }
            }

            // wordmark

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "FlatSplit",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = TextPrimary,
                modifier = Modifier.graphicsLayer {
                    alpha = wordmarkAlpha.value
                    translationY = wordmarkOffset.value
                }
            )


            Text(
                text = "Shared expenses, made simple.",
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .graphicsLayer {
                        alpha = taglineAlpha.value
                        translationY = taglineOffset.value
                    }
            )

            Spacer(modifier = Modifier.height(94.dp))

            // loader

            Row(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = loaderAlpha.value
                    },
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {

                val loaderColors = listOf(
                    AccentCoral,
                    AccentGold,
                    PositiveTeal
                )

                loaderColors.forEachIndexed { index, color ->

                    PulsingDot(
                        color = color,
                        delayMillis = index * 200
                    )
                }
            }
        }
    }





}
// pulse loading dot
@Composable
private fun PulsingDot(
    color: Color,
    delayMillis: Int
) {
    val transition = rememberInfiniteTransition(label = "loader dot")

    val scale by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
                delayMillis = delayMillis
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(7.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(color)
    ) { }
}
@Composable
private fun DotGridBackground(
    dotColor: Color
) {

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        val spacing = 40.dp.toPx()
        val radius = 1.5.dp.toPx()

        var y = -4.dp.toPx()

        while (y < size.height){
            var x = -4.dp.toPx()

            while (x < size.width){
                drawCircle(
                    color = dotColor,
                    radius = radius,
                    center = Offset(x, y)
                )

                x += spacing
            }
            y += spacing
        }
    }
}