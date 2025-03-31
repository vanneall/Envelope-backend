package com.point.chats.events.data.entities

import com.fasterxml.jackson.annotation.JsonTypeName
import com.point.chats.chatsv2.data.entity.event.MessageSentEvent
import com.point.chats.common.data.entities.Event
import com.point.chats.events.rest.requests.CreateMessageRequest
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message")
@JsonTypeName("message")
data class Message(
    override val id: String = UUID.randomUUID().toString(),
    val timestamp: Instant = Instant.now(),
    val senderId: String,
    val content: String,
    val photos: MutableList<Long>?,
) : Event

fun CreateMessageRequest.toMessageEvent(photos: MutableList<Long>? = null) = MessageSentEvent(
    senderId = senderId,
    text = content,
)