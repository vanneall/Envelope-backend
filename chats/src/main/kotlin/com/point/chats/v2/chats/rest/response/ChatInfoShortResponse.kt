package com.point.chats.v2.chats.rest.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChatInfoShortResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("photo")
    val photo: Long? = null,
    @JsonProperty("last_message")
    val lastMessage: MessageResponse? = null,
)

data class MessageResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("text")
    val text: String,
    @JsonProperty("timestamp")
    val timestamp: Instant,
)

fun ChatInfoShort.toResponse() = ChatInfoShortResponse(
    id = id,
    name = name,
    photo = photo,
    lastMessage = lastMessage?.toResponse(),
)

fun Message.toResponse() = MessageResponse(
    id = id,
    text = text,
    timestamp = timestamp,
)

data class ChatInfoShort(
    val id: String,
    val name: String,
    val photo: Long? = null,
    val lastMessage: Message? = null,
)

data class Message(
    val id: String,
    val text: String,
    val timestamp: Instant,
)