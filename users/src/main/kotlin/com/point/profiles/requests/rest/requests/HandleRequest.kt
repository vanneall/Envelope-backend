package com.point.profiles.requests.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class HandleRequest(
    @JsonProperty("result")
    val result: RequestHandleType,
)

enum class RequestHandleType{
    REJECT,
    ACCEPT,
}