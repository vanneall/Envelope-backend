package com.point.authorization.service.user

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorInfo(
    @JsonProperty("code")
    val code: String,
)