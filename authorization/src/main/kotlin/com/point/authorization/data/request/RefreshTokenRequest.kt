package com.point.authorization.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.authorization.error.codes.ConstantCodes.BLANK_ARG
import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = BLANK_ARG)
    @JsonProperty("refresh_token")
    val refreshToken: String
)
