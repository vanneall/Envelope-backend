package com.point.chats.events.data.persistance.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.point.chats.events.data.rest.meta.MessageMeta
import com.point.chats.v2.chats.rest.response.UserLightweightInfoResponse
import org.springframework.data.annotation.TypeAlias
import java.time.Instant
import java.util.*

@TypeAlias("message")
@JsonTypeName("message")
data class MessageEvent(
    @JsonProperty("id")
    override val id: String = UUID.randomUUID().toString(),
    @JsonProperty("timestamp")
    override val timestamp: Instant = Instant.now(),
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("content")
    val content: String,
    @JsonProperty("attachments")
    val attachments: List<Long> = emptyList(),
    @JsonProperty("is_pinned")
    val isPinned: Boolean,
    @JsonProperty("is_edited")
    val isEdited: Boolean,
) : BaseEvent

fun MessageEvent.toMessageMeta(userLightweightInfoResponse: UserLightweightInfoResponse) = MessageMeta(
    id = id,
    timestamp = timestamp,
    userName = userLightweightInfoResponse.name,
    userPhotoId = userLightweightInfoResponse.photoId,
    senderId = senderId,
    text = content,
    attachments = attachments.map { it },
    isPinned = isPinned,
    isEdited = isEdited,
)
