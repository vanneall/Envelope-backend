package com.point.chats.common.data.entities

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.point.chats.events.data.entities.Message
import com.point.chats.participants.rest.errors.exceptions.MessageNotFoundException
import javax.management.Notification

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Message::class, name = "message"),
    JsonSubTypes.Type(value = Notification::class, name = "notification")
)
interface Event {
    val id: String
}

fun List<Event>.requiredMessageById(messageId: String): Message =
    (first { event -> event.id == messageId && event is Message} as? Message)
        ?: throw MessageNotFoundException(messageId)