package com.point.chats.events.data.rest.commands

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateMessageRequest2(
    @JsonProperty("content")
    val content: String,
    @JsonProperty("photos")
    val photos: MutableList<Long>? = null,
)