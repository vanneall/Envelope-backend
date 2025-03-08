package com.point.profiles.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class UserId(
    @JsonProperty("id")
    val userId: String
)