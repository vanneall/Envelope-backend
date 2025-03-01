package com.point.chats.chats.rest.requests

import org.springframework.web.multipart.MultipartFile

data class CreateChatRequest(
    val name: String,
    val description: String?,
    val participants: List<String>,
    val ownerId: String,
    val photo: MultipartFile? = null,
)