package com.point.authorization.data.domain

import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.utils.PasswordHasher
import java.util.*

data class User(
    val id: UUID? = null,
    val username: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String,
)

fun UserRegistrationRequest.toUser() = User(
    username = username,
    email = email,
    phoneNumber = telephone,
    password = password,
)