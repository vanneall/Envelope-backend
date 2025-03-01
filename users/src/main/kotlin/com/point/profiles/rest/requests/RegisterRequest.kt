package com.point.profiles.rest.requests

import org.springframework.web.multipart.MultipartFile
import java.util.*

data class RegisterRequest(
    val id: String,
    val name: String,
    val birthDate: Date = Date(),
    val photo: MultipartFile? = null,
)