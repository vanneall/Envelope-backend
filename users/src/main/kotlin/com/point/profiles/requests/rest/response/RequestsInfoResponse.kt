package com.point.profiles.requests.rest.response

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestsInfoResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("last_photo")
    val lastPhoto: Long?,
)