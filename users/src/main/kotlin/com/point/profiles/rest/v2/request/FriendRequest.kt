package com.point.profiles.rest.v2.request

import com.fasterxml.jackson.annotation.JsonProperty

data class FriendRequest(
    @JsonProperty("friend_user_id")
    val otherId: String,
)
