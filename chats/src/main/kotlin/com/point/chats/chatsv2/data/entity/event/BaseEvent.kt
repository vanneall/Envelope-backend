package com.point.chats.chatsv2.data.entity.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.point.chats.events.data.entities.Message
import java.time.Instant
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
@JsonIgnoreProperties(ignoreUnknown = true)
interface BaseEvent {
    val id: String
    val timestamp: Instant
}
