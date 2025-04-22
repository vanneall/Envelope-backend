package com.point.profiles.requests.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateRequest(
    @JsonProperty("user_id")
    val userId: String,
)