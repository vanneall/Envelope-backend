package com.point.chats.events.data.rest.commands

import com.fasterxml.jackson.annotation.JsonProperty

data class UserId(
    @JsonProperty("username")
    val username: String,
)