package com.techhub.flatsplit.presentation.settleup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.model.settlement.SettlementSuggestion
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.NegativeRose
import com.techhub.flatsplit.ui.theme.NegativeRoseBg
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun SettlementSuggestionCard(
    suggestion: SettlementSuggestion,
    fromName: String,
    toName: String,
    currentUserId: String,
    pendingSettlement: Settlement?,
    onPayWithCash: () -> Unit,
    onConfirm: () -> Unit,
    onNotReceived: () -> Unit,
    onRequest: () -> Unit,
    onReportManualUpiPayment: () -> Unit
) {

    val dimens = AppTheme.dimensions

    var showPaymentDialog by remember { mutableStateOf(false) }

    val isUserDebtor = suggestion.fromUserId == currentUserId

    val isUserCreditor = suggestion.toUserId == currentUserId

    val settlementStatus = pendingSettlement?.status

    var showRecordConfirmation by remember {
        mutableStateOf(false)
    }

    var paymentMethodToRecord by remember {
        mutableStateOf<String?>(null)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BorderSubtle
        ),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceRaised
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.large)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                InitialNameCircle(
                    name = fromName,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(
                    modifier = Modifier.width(dimens.large)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "$fromName → $toName",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.size(4.dp)
                    )

                    Text(
                        text = formatMoney(suggestion.amount),
                        color = TextSecondary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                InitialNameCircle(
                    name = toName,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(
                modifier = Modifier.size(dimens.medium)
            )

            when {
                // Debtor: no active request or payment
                isUserDebtor && settlementStatus == null -> {
                    Button(
                        onClick = {
                            showPaymentDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGold
                        )
                    ) {
                        Text("Settle")
                    }
                }

                // Debtor: creditor has requested payment
                isUserDebtor && settlementStatus == "REQUESTED" -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$toName requested payment",
                            color = AccentGold,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Button(
                            onClick = {
                                showPaymentDialog = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentGold
                            )
                        ) {
                            Text("Pay now")
                        }
                    }
                }

                // Debtor: payment reported, waiting for creditor
                isUserDebtor && settlementStatus == "PENDING" -> {
                    Text(
                        text = "Waiting for confirmation",
                        modifier = Modifier.fillMaxWidth(),
                        color = NegativeRose,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Creditor: no active request exists
                isUserCreditor && settlementStatus == null -> {
                    Button(
                        onClick = onRequest,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGold
                        )
                    ) {
                        Text("Request")
                    }
                }

                // Creditor: request sent, awaiting debtor's payment
                isUserCreditor && settlementStatus == "REQUESTED" -> {
                    Text(
                        text = "Payment request sent to $fromName",
                        modifier = Modifier.fillMaxWidth(),
                        color = AccentGold,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Creditor: debtor reports payment made
                isUserCreditor && settlementStatus == "PENDING" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentGold
                            )
                        ) {
                            Text(
                                text = "Received",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }

                        Button(
                            onClick = onNotReceived,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NegativeRoseBg
                            )
                        ) {
                            Text(
                                text = "Not received",
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }        }
    }

    // Payment method dialog
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = {
                showPaymentDialog = false
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Choose payment method",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            showPaymentDialog = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NegativeRose
                        )
                    }
                }
            },
            text = {
                Text(
                    "You owe $toName ${formatMoney(suggestion.amount)}. " +
                            "Choose the payment method you already used. " +
                            "FlatSplit records the payment; it does not transfer money."
                )
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            showPaymentDialog = false
                            paymentMethodToRecord = "UPI"
                            showRecordConfirmation = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGold
                        )
                    ) {
                        Text(
                            text = "Online / UPI",
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = {
                            showPaymentDialog = false
                            paymentMethodToRecord = "CASH"
                            showRecordConfirmation = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGold
                        )
                    ) {
                        Text(
                            text = "Cash",
                            color = Color.Black
                        )
                    }
                }
            },
            containerColor = SurfaceRaised
        )
    }

    if (showRecordConfirmation) {
        val methodLabel = when (paymentMethodToRecord) {
            "UPI" -> "online"
            "CASH" -> "cash"
            else -> "unknown"
        }

        AlertDialog(
            onDismissRequest = {
                showRecordConfirmation = false
                paymentMethodToRecord = null
            },
            title = {
                Text("Record $methodLabel payment?")
            },
            text = {
                Text(
                    "Continue only if you have actually completed " +
                            "the payment. Your flatmate must still confirm " +
                            "receipt before balances change."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val method = paymentMethodToRecord

                        showRecordConfirmation = false
                        paymentMethodToRecord = null

                        when (method) {
                            "UPI" -> onReportManualUpiPayment()
                            "CASH" -> onPayWithCash()
                        }
                    },
                    modifier = Modifier.padding(start = 30.dp)
                ) {
                    Text(
                        text = "Confirm payment",
                        fontWeight = FontWeight.Medium
                        )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRecordConfirmation = false
                        paymentMethodToRecord = null
                    },
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                        )
                }
            },
            containerColor = SurfaceRaised
        )
    }
}

private fun formatMoney(amountPaise: Long): String {
    return if (amountPaise % 100 == 0L) {
        "₹%,d".format(amountPaise / 100)
    } else {
        "₹%,.2f".format(amountPaise / 100.0)
    }
}

@Composable
private fun InitialNameCircle(
    name: String,
    modifier: Modifier = Modifier
) {

    val initialName = name
        .firstOrNull()
        ?.uppercase()
        ?: "?"

    Box(
        modifier = modifier
            .background(
                color = avatarColor(name),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initialName,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
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
    val index =
        kotlin.math.abs(name.hashCode()) % avatarColors.size

    return avatarColors[index]
}