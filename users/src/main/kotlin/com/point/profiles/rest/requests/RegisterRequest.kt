package com.point.profiles.rest.requests

import java.util.*

data class RegisterRequest(
    val id: String,
    val name: String,
    val birthDate: Date = Date(),
)