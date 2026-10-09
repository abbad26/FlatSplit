package com.techhub.flatsplit.presentation.settleup.upi

data class UpiPaymentResult(
    val status: String?,
    val transactionId: String?,
    val transactionRef: String?,
    val responseCode: String?
)