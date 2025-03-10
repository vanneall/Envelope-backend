package com.point.profiles.rest.v2.request

import com.fasterxml.jackson.annotation.JsonProperty

data class BlockedUserRequest(
    @JsonProperty("blocked_user_id")
    val blockedUserId: String,
)
