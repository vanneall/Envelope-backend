package com.point.chats.events.data.rest.commands

import com.fasterxml.jackson.annotation.JsonProperty

data class UnpinMessage(
    @JsonProperty("message_id")
    val messageId: String,
)