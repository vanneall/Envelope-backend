package com.point.chats.events.data.persistance.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("created")
@JsonTypeName("created")
data class ChatCreatedEvent(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
) : BaseEvent