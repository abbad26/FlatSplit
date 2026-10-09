package com.techhub.flatsplit.presentation.settleup.upi

import android.content.Intent
import android.net.Uri

fun parseUpiPaymentResult(
    intent: Intent?
): UpiPaymentResult {

    if (intent == null) {
        return UpiPaymentResult(
            status = null,
            transactionId = null,
            transactionRef = null,
            responseCode = null
        )
    }

    val responseString = intent.getStringExtra("response")

    val uri: Uri? =
        responseString?.let {
            Uri.parse("upi://pay?$it")
        } ?: intent.data

    return UpiPaymentResult(
        status = uri?.getQueryParameter("Status")
            ?: intent.getStringExtra("Status"),

        transactionId = uri?.getQueryParameter("txnId")
            ?: intent.getStringExtra("txnId"),

        transactionRef = uri?.getQueryParameter("txnRef")
            ?: intent.getStringExtra("txnRef")
            ?: uri?.getQueryParameter("tr"),

        responseCode = uri?.getQueryParameter("responseCode")
            ?: intent.getStringExtra("responseCode")
    )
}