package com.techhub.flatsplit.domain.model

data class Feedback(
    val category: String,
    val message: String,
    val rating: Int? = null
)