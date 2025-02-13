package com.point.chats.events.data.entities

import com.fasterxml.jackson.annotation.JsonTypeName
import com.point.chats.common.data.entities.Event
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("notification")
@JsonTypeName("notification")
data class Notification(
    override val id: String = UUID.randomUUID().toString(),
    val timestamp: Instant = Instant.now(),
    val type: Type,
    val text: String? = null,
) : Event {
    enum class Type {
        INVITE,
        PIN_MESSAGE
    }
}