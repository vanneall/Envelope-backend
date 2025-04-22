package com.point.profiles.users.data

data class UserLightweightInfo(
    val username: String,
    val name: String,
    val photoId : Long? = null,
)