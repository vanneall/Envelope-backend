package com.point.profiles.errors

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("code")
    val errorCode: String,
)
