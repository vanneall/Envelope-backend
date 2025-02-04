package com.point.authorization.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRegistrationRequest(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String,
    @JsonProperty("telephone")
    val telephone: String? = null,
    @JsonProperty("email")
    val email: String? = null,
    @JsonProperty("is_dev")
    val isDeveloper: Boolean = false,
)
