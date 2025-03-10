package com.point.profiles.rest.v2.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UserInfoRequestBody(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("about_user")
    val aboutUser: String,
    @JsonProperty("birth_date")
    val birthDate: LocalDate,
)