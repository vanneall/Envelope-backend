package com.point.profiles.users.rest.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.profiles.users.data.UserLightweightInfo

data class UserLightweightInfoResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("photo")
    val photoId : Long? = null,
)

fun UserLightweightInfo.toResponse() = UserLightweightInfoResponse(
    username = username,
    name = name,
    photoId = photoId,
)
