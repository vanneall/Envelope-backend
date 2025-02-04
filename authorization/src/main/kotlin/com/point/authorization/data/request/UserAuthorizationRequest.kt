package com.point.authorization.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UserAuthorizationRequest(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String,
)