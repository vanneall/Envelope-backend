package com.point.chats.v2.chats.rest.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserLightweightInfoResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("photo")
    val photoId : Long? = null,
)