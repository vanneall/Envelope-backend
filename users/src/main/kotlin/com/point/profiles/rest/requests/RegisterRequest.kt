package com.point.profiles.rest.requests

import java.util.*

data class RegisterRequest(
    val id: UUID,
    val name: String,
    val birthDate: Date = Date(),
)