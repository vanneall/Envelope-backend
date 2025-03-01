package com.point.chats.chats.rest.requests

import org.springframework.web.multipart.MultipartFile

data class ChatUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val photo: MultipartFile? = null
)