package com.techhub.flatsplit.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderDefault
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.NegativeRoseBg
import com.techhub.flatsplit.ui.theme.PositiveTeal
import com.techhub.flatsplit.ui.theme.PositiveTealBg
import com.techhub.flatsplit.ui.theme.SurfaceDark
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextFaint
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary
import com.techhub.flatsplit.ui.theme.largeDimensions


@Composable
fun CustomDialog(
    title: String,
    placeholder: String,
    confirmText: String = "Continue",
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    val dimens = AppTheme.dimensions
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {


        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceDark
            ),
            elevation = CardDefaults.elevatedCardElevation(5.dp) ,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .border(1.dp, color = AccentGold, shape = RoundedCornerShape(15.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = placeholder,
                            color = TextFaint,
                            fontSize = 13.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        // Text
                        focusedTextColor   = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        disabledTextColor  = TextFaint,

                        // Container
                        focusedContainerColor   = SurfaceRaised,
                        unfocusedContainerColor = SurfaceRaised,
                        disabledContainerColor  = SurfaceDark,

                        // Border
                        focusedIndicatorColor   = BorderSubtle,
                        unfocusedIndicatorColor = BorderDefault,
                        disabledIndicatorColor  = BorderSubtle,

                        // Placeholder
                        focusedPlaceholderColor   = TextFaint,
                        unfocusedPlaceholderColor = TextFaint,

                        // Cursor
                        cursorColor = PositiveTeal,
                    )

                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.large),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.size(width = 120.dp, height = 40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NegativeRoseBg,
                            contentColor = TextPrimary
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = { onConfirm() },
                        modifier = Modifier.size(width = 120.dp, height = 40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PositiveTealBg,
                            contentColor = TextPrimary
                        )
                    ) { Text(
                        text = confirmText,
                        fontSize = 16.sp
                    )}
                }



            }

        }
    }
}

