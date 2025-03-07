package com.point.chats.common.data.entities

import com.point.chats.chats.rest.requests.ChatUpdateRequest
import com.point.chats.chats.rest.requests.CreateChatRequest
import com.point.chats.participants.rest.errors.exceptions.UserFreezeException
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chats")
data class Chat(
    @Id
    val id: String? = null,
    var name: String,
    var description: String? = null,
    var pinnedMessage: String? = null,
    val photos: MutableList<Long> = mutableListOf(),
    val users: MutableSet<String> = mutableSetOf(),
    val activeUserAuthorities: MutableMap<String, UserAuthorities> = mutableMapOf(),
    val freezeUsers: MutableMap<String, FreezeReasons> = mutableMapOf(),
    val events: MutableList<Event> = mutableListOf(),
)


fun Chat.addActiveParticipant(uuid: String) {
    users.add(uuid)
    activeUserAuthorities[uuid] = UserAuthorities()
}

fun Chat.removeActiveParticipant(uuid: String) {
    users.remove(uuid)
    activeUserAuthorities.remove(uuid)
}

fun Chat.addFreezeParticipant(uuid: String) {
    freezeUsers[uuid] = FreezeReasons()
}

fun Chat.removeFreezeParticipant(uuid: String) {
    freezeUsers.remove(uuid)
}

fun CreateChatRequest.toChat(photos: MutableList<Long>) = Chat(
    name = name,
    description = description,
    photos = photos,
    activeUserAuthorities = participants
        .associateBy({ it }, { UserAuthorities(isAdmin = it == ownerId) })
        .toMutableMap(),
    users = participants.toMutableSet(),
)

fun Chat.update(newChat: ChatUpdateRequest, photoId: Long? = null) {
    name = newChat.name ?: name
    description = newChat.description ?: description
    photoId?.let { photos.addFirst(it) }
}

fun Chat.requireUserAuthorities(userId: String): UserAuthorities {
    val userAuthorities = activeUserAuthorities[userId]
    if (userAuthorities == null) {
        freezeUsers[userId] ?: throw UserFreezeException(userId)
        throw UserFreezeException(userId)
    }
    return userAuthorities
}
