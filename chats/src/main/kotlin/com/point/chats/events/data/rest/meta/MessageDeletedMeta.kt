package com.point.chats.events.data.rest.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message_delete")
@JsonTypeName("message_delete")
data class MessageDeletedMeta(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
    @JsonProperty("message_id")
    val deletedMessageId: String,
): BaseMeta