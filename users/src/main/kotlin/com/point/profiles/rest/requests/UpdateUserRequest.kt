package com.point.profiles.rest.requests

import org.springframework.web.multipart.MultipartFile

data class UpdateUserRequest(
    val name: String? = null,
    val status: String? = null,
    val photo: MultipartFile? = null,
)
