package com.point.chats.events.data.rest.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.TypeAlias
import java.time.Instant

@TypeAlias("message")
@JsonTypeName("message")
data class MessageMeta(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("timestamp")
    override val timestamp: Instant,
    @JsonProperty("userName")
    val userName: String,
    @JsonProperty("userPhotoId")
    val userPhotoId: Long?,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("attachments")
    val attachments: List<Long> = emptyList(),
    @JsonProperty("is_pinned")
    val isPinned: Boolean,
    @JsonProperty("is_edited")
    val isEdited: Boolean,
) : BaseMeta