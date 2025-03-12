package com.point.authorization.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UserRegistrationRequest(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("about_user")
    val aboutUser: String? = null,
    @JsonProperty("birth_date")
    val birthDate: LocalDate,
    @JsonProperty("is_dev")
    val isDeveloper: Boolean = false,
)
