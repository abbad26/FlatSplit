package com.techhub.flatsplit.domain.model.settlement

import com.google.firebase.Timestamp

data class Expense(
    val id: String = "",
    val roomId: String = "",
    val title: String = "",
    val amount: Long = 0L,
    val category: String = "",
    val paidBy: String = "",
    val splitBetween: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now()
)