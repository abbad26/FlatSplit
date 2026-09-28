package com.techhub.flatsplit.presentation.addexpenses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.TextPrimary

@Composable
fun PaidByDropdown(
    members: List<ExpenseMember>,
    selectedUserId: String,
    onUserSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMember = members.firstOrNull { it.id == selectedUserId }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                expanded = true
            },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceDark
            ),
            border = BorderStroke(
                1.dp,
                BorderSubtle
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedMember?.name ?: "Select member",
                    modifier = Modifier.weight(1f),
                    color = TextPrimary
                )

                Text(
                    text = "⌄",
                    color = TextPrimary,
                    fontSize = 22.sp
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            members.forEach { member ->

                DropdownMenuItem(
                    text = {
                        Text(member.name)
                    },
                    onClick = {
                        onUserSelected(member.id)
                        expanded = false
                    }
                )
            }
        }
    }
}