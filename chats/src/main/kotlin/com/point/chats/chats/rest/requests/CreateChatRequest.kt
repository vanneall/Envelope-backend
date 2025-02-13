package com.point.chats.chats.rest.requests

data class CreateChatRequest(
    val name: String,
    val description: String?,
    val participants: List<String>,
    val ownerId: String,
    val photo: String?
)