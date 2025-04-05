package com.point.chats.events.data.rest.meta

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageMeta::class, name = "message"),
    JsonSubTypes.Type(value = MessageDeletedMeta::class, name = "message_delete"),
    JsonSubTypes.Type(value = MessageEditedMeta::class, name = "message_edit"),
    JsonSubTypes.Type(value = MessagePinnedMeta::class, name = "message_pin"),
    JsonSubTypes.Type(value = MessageStopTypedMeta::class, name = "message_type_stop"),
    JsonSubTypes.Type(value = MessageTypedMeta::class, name = "message_type"),
    JsonSubTypes.Type(value = MessageUnpinnedMeta::class, name = "message_unpin"),
)
interface BaseMeta {
    val id: String
    val timestamp: Instant
}