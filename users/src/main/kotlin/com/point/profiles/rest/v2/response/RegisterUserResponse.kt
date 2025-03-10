package com.point.profiles.rest.v2.response

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterUserResponse(
    @JsonProperty("username")
    val username: String,
)