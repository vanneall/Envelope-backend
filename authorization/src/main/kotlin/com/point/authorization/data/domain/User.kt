package com.point.authorization.data.domain

import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.service.UserInfoRequestBody
import java.time.LocalDate

data class User(
    val username: String,
    val name: String,
    val status: String? = null,
    val aboutUser: String? = null,
    val birthDate: LocalDate,
    val password: String,
)

fun UserRegistrationRequest.toUser() = User(
    username = username,
    name = name,
    status = status,
    aboutUser = aboutUser,
    birthDate = birthDate,
    password = password,
)

fun User.toUserInfoRequestBody() = UserInfoRequestBody(
    username = username,
    name = name,
    status = status,
    aboutUser = aboutUser,
    birthDate = birthDate,
)