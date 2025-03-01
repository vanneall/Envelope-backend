package com.point.chats.events.rest.requests

import org.springframework.web.multipart.MultipartFile

data class CreateMessageRequest(
    val senderId: String,
    val content: String,
    val photos: MutableList<MultipartFile>? = null,
)