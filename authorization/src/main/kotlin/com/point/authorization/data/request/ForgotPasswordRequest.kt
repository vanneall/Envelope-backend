package com.point.authorization.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.authorization.error.codes.ConstantCodes.BLANK_ARG
import jakarta.validation.constraints.NotBlank

class ForgotPasswordRequest(
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("username")
    val username: String,
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("new_password")
    val newPassword: String,
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("confirm_password")
    val confirmPassword: String,
)