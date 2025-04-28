package com.point.chats.v2.chats.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteChatsRequest(
    @JsonProperty("chat_ids")
    val chatIds: List<String>,
)