package com.point.profiles.rest.v2.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.profiles.services.UserLightweightInfo

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
