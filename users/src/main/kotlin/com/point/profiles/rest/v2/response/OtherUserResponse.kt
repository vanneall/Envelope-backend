package com.point.profiles.rest.v2.response

import com.fasterxml.jackson.annotation.JsonProperty

data class OtherUserResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("last_photo")
    val lastPhoto: Long?,
)