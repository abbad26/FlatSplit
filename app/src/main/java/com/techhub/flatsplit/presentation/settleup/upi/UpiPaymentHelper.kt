package com.techhub.flatsplit.presentation.settleup.upi

import android.content.Intent
import android.net.Uri
import java.util.Locale

fun createUpiPaymentIntent(
    upiId: String,
    payeeName: String,
    amountPaise: Long,
    note: String
): Intent {

    require(
        upiId.isNotBlank() &&
                upiId.contains("@")
    ) {
        "Enter the recipient's actual UPI ID."
    }

    require(amountPaise > 0L) {
        "Payment amount must be greater than zero."
    }

    // Convert paise to rupees without floating-point arithmetic.
    val amountRupees = String.format(
        Locale.US,
        "%d.%02d",
        amountPaise / 100,
        amountPaise % 100
    )

    val transactionRef = "FS${System.currentTimeMillis()}"

    val uri = Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", upiId.trim())
        .appendQueryParameter("pn", payeeName)
        .appendQueryParameter("tr", transactionRef)
        .appendQueryParameter("am", amountRupees)
        .appendQueryParameter("cu", "INR")
        .appendQueryParameter("mode", "00")
        .appendQueryParameter("tn", note)
        .build()

    return Intent(Intent.ACTION_VIEW, uri)
}