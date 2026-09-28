package com.techhub.flatsplit.domain.model.settlement

import com.google.firebase.Timestamp

data class Settlement(
    val id: String = "",
    val roomId: String = "",
    val fromUserId: String = "",
    val toUserId: String = "",
    val amount: Long = 0L,
    val status: String = "PENDING",
    val createdAt: Timestamp = Timestamp.now(),
    val confirmedAt: Timestamp? = null
)