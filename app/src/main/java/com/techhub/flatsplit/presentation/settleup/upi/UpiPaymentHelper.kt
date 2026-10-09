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

    val amountRupees = String.format(
        Locale.US,
        "%.2f",
        amountPaise / 100.0
    )

    val uri = Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", upiId)
        .appendQueryParameter("pn", payeeName)
        .appendQueryParameter("am", amountRupees)
        .appendQueryParameter("cu", "INR")
        .appendQueryParameter("tn", note)
        .build()

    return Intent(Intent.ACTION_VIEW, uri)
}