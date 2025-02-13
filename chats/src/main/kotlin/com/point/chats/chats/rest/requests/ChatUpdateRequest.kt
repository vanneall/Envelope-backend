package com.point.chats.chats.rest.requests

data class ChatUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val photo: String? = null
)