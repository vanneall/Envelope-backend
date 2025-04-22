package com.point.profiles.profile.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.profiles.profile.errors.codes.ConstantCodes.BLANK_ARG
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate

data class UserInfoRequestBody(
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("username")
    val username: String,
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("about_user")
    val aboutUser: String? = null,
    @field:PastOrPresent()
    @JsonProperty("birth_date")
    val birthDate: LocalDate = LocalDate.now(),
)