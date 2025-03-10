package com.point.profiles.exceptions

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("error_code")
    val errorCode: String,
)
