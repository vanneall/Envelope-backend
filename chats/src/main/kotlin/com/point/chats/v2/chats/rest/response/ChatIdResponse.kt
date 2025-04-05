package com.point.chats.v2.chats.rest.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatIdResponse(
    @JsonProperty("id")
    val id: String,
)