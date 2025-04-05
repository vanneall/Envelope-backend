package com.point.chats.events.data.persistance.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageEvent::class, name = "message"),
    JsonSubTypes.Type(value = ChatCreatedEvent::class, name = "created"),
)
@JsonIgnoreProperties(ignoreUnknown = true)
interface BaseEvent {
    val id: String
    val timestamp: Instant
}
