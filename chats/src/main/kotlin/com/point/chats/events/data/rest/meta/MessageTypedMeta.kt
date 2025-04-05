package com.point.chats.events.data.rest.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message_type")
@JsonTypeName("message_type")
data class MessageTypedMeta(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
): BaseMeta