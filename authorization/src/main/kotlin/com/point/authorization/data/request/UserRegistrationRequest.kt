package com.point.authorization.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.authorization.error.codes.ConstantCodes.BLANK_ARG
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UserRegistrationRequest(
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("username")
    val username: String,
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("password")
    val password: String,
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("about_user")
    val aboutUser: String? = null,
    @JsonProperty("birth_date")
    val birthDate: LocalDate = LocalDate.now(),
    @JsonProperty("is_dev")
    val isDeveloper: Boolean = false,
    @JsonProperty("email")
    val email: String,
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("code")
    val code: Int,
)
