package com.point.chats.chatsv2.data.entity.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("join")
@JsonTypeName("join")
data class UserJoinedEvent(
    val userId: String, val invitedBy: String?,
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),

    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now()
) : BaseEvent
