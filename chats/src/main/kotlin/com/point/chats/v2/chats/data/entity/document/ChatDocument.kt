package com.point.chats.v2.chats.data.entity.document

import com.point.chats.events.data.persistance.entities.BaseEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chats")
data class ChatDocument(
    @Id
    val id: String? = null,
    val type: ChatType,
    val participants: List<ChatUser>,
    val name: String? = null,
    val description: String? = null,
    val photos: List<Long>,
    val events: MutableList<BaseEvent> = mutableListOf(),
    val pinnedMessage: BaseEvent? = null,
)

enum class ChatType {
    PRIVATE,
    ONE_ON_ONE,
    MANY,
    ;
}

open class ChatUser(
    val id: String,
) {

    override fun equals(other: Any?) = other is ChatUser && id == other.id

    override fun toString() = "ChatUser(id=$id)"

    override fun hashCode() = id.hashCode()
}

class GroupChatUser(
    id: String,
    val userRole: UserRole,
) : ChatUser(id) {

    override fun equals(other: Any?) = other is GroupChatUser && id == other.id

    override fun hashCode() = id.hashCode()

    override fun toString() = "GroupChatUser(id=$id, userRole=$userRole)"
}

data class UserRole(
    val name: String,
    val canSentMessages: Boolean,
    val canInviteUsers: Boolean,
    val canPinMessages: Boolean,
    val canSetRoles: Boolean,
    val canDeleteUsers: Boolean,
) {
    companion object {
        val admin = UserRole(
            name = "admin",
            canSentMessages = true,
            canInviteUsers = true,
            canPinMessages = true,
            canSetRoles = true,
            canDeleteUsers = true,
        )

        val simple = UserRole(
            name = "simple",
            canSentMessages = true,
            canInviteUsers = false,
            canPinMessages = true,
            canSetRoles = false,
            canDeleteUsers = false,
        )
    }
}