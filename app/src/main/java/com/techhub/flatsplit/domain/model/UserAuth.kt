package com.techhub.flatsplit.domain.model

data class UserAuth(
    val uid: String,
    val name: String,
    val email: String,
    val profileImage: String?
)