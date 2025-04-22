package com.point.profiles.requests.rest.response

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestResponse(
    @JsonProperty("id")
    val id: Long,
)