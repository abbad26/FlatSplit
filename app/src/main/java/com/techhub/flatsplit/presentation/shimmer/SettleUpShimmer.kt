package com.techhub.flatsplit.presentation.shimmer


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AppTheme

@Composable
fun SettleUpShimmer(
    modifier: Modifier = Modifier
) {

    val dimens = AppTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.large),
        verticalArrangement = Arrangement.spacedBy(dimens.large)
    ) {

        // Flat switcher
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp)
        )

        // Balance card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .padding(dimens.large),
            verticalArrangement = Arrangement.spacedBy(dimens.medium)
        ) {

            ShimmerBox(
                modifier = Modifier.size(
                    width = 110.dp,
                    height = 18.dp
                )
            )

            ShimmerBox(
                modifier = Modifier.size(
                    width = 150.dp,
                    height = 38.dp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerValue()
                ShimmerValue()
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerValue()
                ShimmerValue()
            }
        }

        // Recent Activity title
        ShimmerBox(
            modifier = Modifier.size(
                width = 170.dp,
                height = 22.dp
            )
        )

        // Expense cards
        repeat(3) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(dimens.large),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                ShimmerBox(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(
                    modifier = Modifier.width(dimens.large)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    ShimmerBox(
                        modifier = Modifier.size(
                            width = 100.dp,
                            height = 18.dp
                        )
                    )

                    ShimmerBox(
                        modifier = Modifier.size(
                            width = 150.dp,
                            height = 14.dp
                        )
                    )
                }

                ShimmerBox(
                    modifier = Modifier.size(
                        width = 70.dp,
                        height = 20.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun ShimmerValue() {

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        ShimmerBox(
            modifier = Modifier.size(
                width = 70.dp,
                height = 14.dp
            )
        )

        ShimmerBox(
            modifier = Modifier.size(
                width = 60.dp,
                height = 20.dp
            )
        )
    }
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerEffect()
    )
}