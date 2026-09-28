package com.techhub.flatsplit.domain.model.settlement

data class SettlementSuggestion(
    val fromUserId: String,
    val toUserId: String,
    val amount: Long
)
