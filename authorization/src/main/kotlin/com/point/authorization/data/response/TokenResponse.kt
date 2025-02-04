package com.point.authorization.data.response

import com.fasterxml.jackson.annotation.JsonProperty

class TokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
)