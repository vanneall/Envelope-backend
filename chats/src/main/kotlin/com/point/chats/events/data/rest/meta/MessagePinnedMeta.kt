package com.point.chats.events.data.rest.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message_pin")
@JsonTypeName("message_pin")
data class MessagePinnedMeta(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
    @JsonProperty("message_id")
    val messageId: String,
    @JsonProperty("content")
    val content: String,
): BaseMeta