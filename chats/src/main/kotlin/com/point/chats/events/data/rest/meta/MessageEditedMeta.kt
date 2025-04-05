package com.point.chats.events.data.rest.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message_edit")
@JsonTypeName("message_edit")
data class MessageEditedMeta(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
    @JsonProperty("edited_message_id")
    val editedMessageId: String,
    @JsonProperty("new_content")
    val newContent: String,
): BaseMeta