package com.techhub.flatsplit.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val upiId: String = "",
    val photoUrl: String? = null
)