package com.point.profiles.rest.requests

data class UpdateUserRequest(
    val name: String? = null,
    val status: String? = null,
    val photos: String? = null
)
