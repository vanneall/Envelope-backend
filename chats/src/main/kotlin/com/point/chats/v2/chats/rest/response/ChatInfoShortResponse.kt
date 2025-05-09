package com.point.chats.v2.chats.rest.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.chats.v2.chats.data.entity.document.ChatType
import java.time.Instant

data class ChatInfoShortResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("type")
    val type: ChatType,
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
    @JsonProperty("type")
    val type: MessageType,
    @JsonProperty("timestamp")
    val timestamp: Instant,
)

enum class MessageType {
    TEXT,
    IMAGE,
    CREATED,
}

fun ChatInfoShort.toResponse() = ChatInfoShortResponse(
    id = id,
    name = name,
    photo = photo,
    type = type,
    lastMessage = lastMessage?.toResponse(),
)

fun Message.toResponse() = MessageResponse(
    id = id,
    text = text,
    type = type,
    timestamp = timestamp,
)

data class ChatInfoShort(
    val id: String,
    val name: String,
    val type: ChatType,
    val photo: Long? = null,
    val lastMessage: Message? = null,
)

data class Message(
    val id: String,
    val text: String,
    val type: MessageType,
    val timestamp: Instant,
)