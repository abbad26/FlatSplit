package com.techhub.flatsplit.domain.model

data class FlatRoom(
    val id: String = "",
    val name: String = "",
    val inviteCode: String = "",
    val createdBy: String = "",
    val memberIds: List<String> = emptyList()
)