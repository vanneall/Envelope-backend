package com.point.authorization.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class EmailRequest(
    @JsonProperty("email")
    val email: String,
)