package com.point.chats.chatsv2.data.entity.event

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message")
@JsonTypeName("message")
@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageSentEvent @JsonCreator constructor(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("attachments")
    val attachments: List<Long> = emptyList(),
) : BaseEvent


@TypeAlias("deleteMessage")
@JsonTypeName("deleteMessage")
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeleteMessageEvent @JsonCreator constructor(
    @JsonProperty("messageId")
    val messageId: String,

    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),

    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now()
) : BaseEvent