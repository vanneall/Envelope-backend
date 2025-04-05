package com.point.chats.events.data.rest.commands

import com.fasterxml.jackson.annotation.JsonProperty

data class EditMessage(
    @JsonProperty("message_id")
    val messageId: String,
    @JsonProperty("content")
    val content: String,
)