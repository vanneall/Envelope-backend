package com.point.chats.v2.chats.rest.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.chats.v2.chats.data.entity.document.ChatType
import com.point.chats.v2.chats.data.entity.document.GroupChatUser
import com.point.chats.v2.chats.data.entity.document.UserRole

data class GroupChatInfo(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("type")
    val type: ChatType,
    @JsonProperty("chat_preview_photo_ids")
    val chatPreviewPhotosIds: List<Long>,
    @JsonProperty("chat_users")
    val users: List<GroupUser>,
    @JsonProperty("media_content_ids")
    val mediaContentIds: List<Long>
)

data class GroupUser(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("photo_id")
    val photoId: Long?,
    @JsonProperty("user_role")
    val userRole: UserRole,
)

fun GroupChatUser.toGroupUser(name: String, photoId: Long?) = GroupUser(
    id = id,
    name = name,
    photoId = photoId,
    userRole = userRole,
)
