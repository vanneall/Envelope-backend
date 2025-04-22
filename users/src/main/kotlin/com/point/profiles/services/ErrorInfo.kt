package com.point.profiles.services

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorInfo(
    @JsonProperty("code")
    val code: String,
)