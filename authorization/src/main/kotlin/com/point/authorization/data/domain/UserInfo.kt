package com.point.authorization.data.domain

import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.service.user.UserInfoRequestBody
import java.time.LocalDate

data class UserInfo(
    val username: String,
    val name: String,
    val status: String? = null,
    val aboutUser: String? = null,
    val birthDate: LocalDate,
    val password: String,
)

fun UserRegistrationRequest.toUserInfo() = UserInfo(
    username = username,
    name = name,
    status = status,
    aboutUser = aboutUser,
    birthDate = birthDate,
    password = password,
)

fun UserInfo.toUserInfoRequestBody() = UserInfoRequestBody(
    username = username,
    name = name,
    status = status,
    aboutUser = aboutUser,
    birthDate = birthDate,
)