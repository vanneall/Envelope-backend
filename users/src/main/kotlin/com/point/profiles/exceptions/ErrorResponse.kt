package com.point.profiles.exceptions

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("code")
    val errorCode: String,
)
