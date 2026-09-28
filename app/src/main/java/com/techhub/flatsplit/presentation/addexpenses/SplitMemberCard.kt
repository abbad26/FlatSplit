package com.techhub.flatsplit.presentation.addexpenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary
import kotlin.math.abs

@Composable
fun SplitMembersCard(
    members: List<ExpenseMember>,
    selectedMembers: Set<String>,
    perPersonPaise: Long,
    onMemberChecked: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceRaised.copy(0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        ) {
            members.forEachIndexed { index, member ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = member.id in selectedMembers,
                        onCheckedChange = {
                            onMemberChecked(member.id)
                        }
                    )

                    InitialAvatar(
                        name = member.name
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Text(
                        text = member.name,
                        modifier = Modifier.weight(1f),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = formatMoney(perPersonPaise),
                        color = TextSecondary
                    )
                }

                if (index < members.lastIndex) {
                    HorizontalDivider(
                        color = BorderSubtle
                    )
                }
            }
        }
    }
}

@Composable
private fun InitialAvatar(
    name: String
) {
    val initial = name
        .firstOrNull()
        ?.uppercase()
        ?: "?"

    BoxAvatar(
        initial = initial,
        color = avatarColor(name)
    )
}

@Composable
private fun BoxAvatar(
    initial: String,
    color: Color
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = color,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = Color.White,
            fontWeight = FontWeight.Bold
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
    val index = abs(name.hashCode()) % avatarColors.size
    return avatarColors[index]
}

private fun formatMoney(amountPaise: Long): String {
    return "₹%,d".format(amountPaise / 100)
}