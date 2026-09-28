package com.techhub.flatsplit.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.domain.model.FlatRoom
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary


@Composable
fun FlatSwitcherDropdown(
    rooms: List<FlatRoom>,
    selectedRoom: FlatRoom?,
    onRoomSelected: (FlatRoom) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.width(160.dp)
            .border(width = 1.dp, color = BorderSubtle, shape = RoundedCornerShape(16.dp))
    ) {

        FlatSwitcher(
            flatName = selectedRoom?.name ?: "Select a flat",
            onClick = { expanded = true}
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            rooms.forEachIndexed { index, room ->

                DropdownMenuItem(
                    text = {
                        Text(room.name)
                    },
                    trailingIcon = {

                        if (room.id == selectedRoom?.id) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Selected"
                            )
                        }
                    },
                    onClick = {
                        onRoomSelected(room)
                        expanded = false
                    }
                )

                if (index < rooms.lastIndex) {
                    HorizontalDivider()
                }
            }
        }    }
}
@Composable
private fun FlatSwitcher(
    flatName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){

    val dimens = AppTheme.dimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SurfaceRaised,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimens.large,
                vertical = dimens.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        FlatSwitcherIcon(
            icon = Icons.Outlined.Home
        )

        Spacer(modifier = Modifier.width(dimens.medium))

        Text(
            text = flatName,
            modifier = Modifier.weight(1f),
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "Switch flat",
            tint = TextPrimary
        )
    }
}

@Composable
private fun FlatSwitcherIcon(
    icon: ImageVector
){

    Box(modifier = Modifier
        .background(
            color = SurfaceRaised,
            shape = RoundedCornerShape(12.dp)
        )
        .padding(8.dp),
        contentAlignment = Alignment.Center
    ){

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentGold
        )
    }
}