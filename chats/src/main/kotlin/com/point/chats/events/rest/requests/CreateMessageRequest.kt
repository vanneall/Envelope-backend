package com.point.chats.events.rest.requests

data class CreateMessageRequest(
    val senderId: String,
    val content: String,
    val photos: List<Long> = emptyList(),
)