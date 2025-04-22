package com.point.profiles.services

import com.fasterxml.jackson.annotation.JsonProperty

data class ResultInfo(
    @JsonProperty("username")
    val username: String,
)