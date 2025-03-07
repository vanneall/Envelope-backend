package com.point.chats.common.data.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatInfoResponse(
    @JsonProperty("id")
    val id: String? = null,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("last_message")
    val lastMessage: String? = null,
)