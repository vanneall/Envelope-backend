package com.point.profiles.profile.rest.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterUserResponse(
    @JsonProperty("username")
    val username: String,
)