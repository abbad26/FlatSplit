package com.techhub.flatsplit.presentation.guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.AccentCoral
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AccentGoldDeep
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary
import androidx.compose.ui.graphics.Color

@Composable
internal fun TipsSection() {
    val dimens = AppTheme.dimensions

    val tips = remember {
        listOf(
            "Check the amount before saving an expense.",
            "Double-check which flatmates are included in the split.",
            "Review your balance before settling up.",
            "Record the payment method you actually used.",
            "Confirm receipt only after you have received the money.",
            "If a payment is unclear, contact your flatmate directly.",
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(modifier = Modifier.padding(dimens.large)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Helpful tips",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimens.medium))
            tips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", color = AccentGoldDeep, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

private data class FaqEntry(
    val question: String,
    val answer: String
)

@Composable
internal fun FaqSection() {
    val dimens = AppTheme.dimensions
    val faqs = remember {
        listOf(
            FaqEntry(
                question = "How do I create or join a flat?",
                answer = "Go to the Flats tab in bottom navigation. Tap 'Create Flat' to start " +
                        "a flat and get an invite code, or tap 'Join Flat' and enter the invite code shared by a flatmate."
            ),
            FaqEntry(
                question = "Why is my payment still pending?",
                answer = "A reported payment stays pending until the recipient taps 'Received' " +
                        "to confirm receipt. If you are unsure whether it arrived, check with them directly."
            ),
            FaqEntry(
                question = "Does FlatSplit transfer money directly?",
                answer = "No. Online / UPI and Cash are payment methods you record in FlatSplit. " +
                        "Any UPI transfer must be made separately in your payment app. FlatSplit " +
                        "does not independently verify a bank transaction."
            ),
            FaqEntry(
                question = "When does a payment affect my balance?",
                answer = "Only after the recipient confirms receipt by tapping 'Received'. " +
                        "A pending payment report does not change the calculated balance."
            ),
            FaqEntry(
                question = "Where can I find recent payment history?",
                answer = "On Settle Up, tap 'History →' beside Settlement Suggestions. It shows " +
                        "recorded settlements for the selected flat over the last 30 days, using " +
                        "the data and dates available in the app. It is not a bank statement."
            ),
            FaqEntry(
                question = "What if the recipient marks a payment as not received?",
                answer = "If the recipient taps 'Not received', the payment report is marked Not " +
                        "received and does not reduce the outstanding balance. Contact the recipient " +
                        "and only report a new payment after you have actually made it."
            )
        )
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Frequently asked questions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(dimens.medium))
        Column(verticalArrangement = Arrangement.spacedBy(dimens.smallMedium)) {
            faqs.forEach { faq -> FaqRow(faq = faq) }
        }
    }
}

@Composable
private fun FaqRow(faq: FaqEntry) {
    var expanded by remember { mutableStateOf(value = false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "faqChevronRotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        colors = CardDefaults.cardColors(containerColor = SurfaceRaised)
    ) {
        Column(modifier = Modifier.padding(AppTheme.dimensions.large)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse answer" else "Expand answer",
                    tint = TextSecondary,
                    modifier = Modifier.rotate(rotation)
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = faq.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
internal fun BottomCta(onDone: () -> Unit) {
    Button(
        onClick = onDone,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentCoral,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = "Got it",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
