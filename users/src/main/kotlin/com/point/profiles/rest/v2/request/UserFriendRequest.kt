package com.point.profiles.rest.v2.request

import com.fasterxml.jackson.annotation.JsonProperty

class UserFriendRequest(
    @JsonProperty("id") val id: Long,
    @JsonProperty("user_id") val userId: String,
)