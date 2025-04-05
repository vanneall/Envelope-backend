package com.point.chats.events.data.rest.commands

import com.fasterxml.jackson.annotation.JsonProperty

data class PinMessage(
    @JsonProperty("message_id")
    val messageId: String,
)