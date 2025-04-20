package com.point.authorization.service.user

import com.fasterxml.jackson.annotation.JsonProperty

data class ResultInfo(
    @JsonProperty("username")
    val username: String,
)