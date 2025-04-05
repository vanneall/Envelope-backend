package com.point.chats.v2.chats.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateChatRequest(
    @JsonProperty("participants")
    val participantIds: List<String>,
    @JsonProperty("name")
    val name: String? = null,
)