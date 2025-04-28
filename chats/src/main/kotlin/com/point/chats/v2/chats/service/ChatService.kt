package com.point.chats.v2.chats.service

import com.point.chats.common.service.ClientService
import com.point.chats.events.data.persistance.entities.ChatCreatedEvent
import com.point.chats.events.data.persistance.entities.MessageEvent
import com.point.chats.events.data.persistance.entities.toMessageMeta
import com.point.chats.events.data.rest.meta.BaseMeta
import com.point.chats.v2.chats.data.entity.document.ChatDocument
import com.point.chats.v2.chats.data.entity.document.ChatType
import com.point.chats.v2.chats.data.entity.document.ChatUser
import com.point.chats.v2.chats.data.entity.document.GroupChatUser
import com.point.chats.v2.chats.data.entity.document.UserRole
import com.point.chats.v2.chats.data.entity.document.addChat
import com.point.chats.v2.chats.data.repository.ChatRepositoryV2
import com.point.chats.v2.chats.data.repository.UserRepository
import com.point.chats.v2.chats.rest.UpdatedUserRole
import com.point.chats.v2.chats.rest.response.ChatInfoShort
import com.point.chats.v2.chats.rest.response.GroupChatInfo
import com.point.chats.v2.chats.rest.response.Message
import com.point.chats.v2.chats.rest.response.toGroupUser
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatService(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepositoryV2,
    private val clientService: ClientService,
) {

    fun createChat(userId: String, participantIds: List<String>, name: String?): String {
        val allParticipants = (listOf(userId) + participantIds).sorted()

        if (!isUsersUnique(allParticipants)) throw IllegalStateException("All participants must be unique")
        if (!isAllUsersExists(allParticipants)) throw IllegalStateException("Some user not exist")

        return if (isOneToOneChat(allParticipants)) {
            createOneToOneChat(allParticipants)
        } else {
            createGroupChat(requireNotNull(name), userId, allParticipants)
        }
    }

    private fun isUsersUnique(participants: List<String>): Boolean {
        val users = mutableSetOf<String>()
        participants.forEach {
            if (users.contains(it)) return false
            users.add(it)
        }
        return true
    }

    private fun isAllUsersExists(participants: List<String>): Boolean {
        val count = userRepository.countByUserIdIn(participants.toMutableSet())
        return count == participants.size.toLong()
    }

    private fun isOneToOneChat(users: List<String>) = users.size == 2

    private fun createOneToOneChat(users: List<String>): String {
        val newOneToOneChat = ChatDocument(
            type = ChatType.ONE_ON_ONE,
            participants = users.map { ChatUser(it) },
            name = null,
            events = mutableListOf(ChatCreatedEvent())
        )
        val chatId = chatRepository.save(newOneToOneChat).id!!
        val firstUser = userRepository.findById(users[0]).get()
        val secondUser = userRepository.findById(users[1]).get()

        firstUser.chatIds.add(chatId)
        userRepository.save(firstUser)

        secondUser.chatIds.add(chatId)
        userRepository.save(secondUser)

        return chatId
    }

    private fun createGroupChat(name: String, adminId: String, users: List<String>): String {
        val newOneToOneChat = ChatDocument(
            type = ChatType.MANY,
            participants = users.map {
                GroupChatUser(
                    id = it,
                    userRole = if (it == adminId) UserRole.admin else UserRole.simple
                )
            },
            name = name,
            events = mutableListOf(ChatCreatedEvent())
        )
        val chatId = chatRepository.save(newOneToOneChat).id!!
        users.forEach { userId ->
            val user = userRepository.findById(userId).get()
            user.chatIds.add(chatId)
            userRepository.save(user)
        }
        return chatId
    }

    fun deleteChat(userId: String, chatId: String) {
        val chat = chatRepository.findById(chatId).orElseThrow {
            IllegalArgumentException("Чат не найден")
        }

        require(userId in chat.participants.map { it.id }) { "Вы не являетесь участником этого чата" }

        chat.participants.map { it.id }.forEach { participantId ->
            val user = userRepository.findById(participantId).orElse(null) ?: return@forEach
            user.chatIds.remove(chatId)
            userRepository.save(user)
        }

        chatRepository.deleteById(chatId)
    }

    fun deleteChats(username: String, chatIds: List<String>) {
        chatIds.forEach { chatId ->
            deleteChat(username, chatId)
        }
    }

    private fun saveChat(chat: ChatDocument): String {
        val savedChat = chatRepository.save(chat)
        savedChat.participants.map { it.id }.forEach { userId ->
            val userDoc = userRepository.findById(userId).orElse(
                com.point.chats.v2.chats.data.entity.document.UserDocument(
                    userId
                )
            )
            userDoc.addChat(savedChat.id!!)
            userRepository.save(userDoc)
        }

        return savedChat.id!!
    }

    fun getUserChats(userId: String, name: String, limit: Int, offset: Int): List<ChatInfoShort> {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("Пользователь не найден")
        }

        val pageable = PageRequest.of(offset, limit)
        val chats = chatRepository.findByIdIn(user.chatIds, pageable).toList()

        val oneToOneUserIds = chats
            .filter { chat ->
                chat.type == ChatType.ONE_ON_ONE
            }
            .flatMap { chat ->
                chat.participants.map { it.id }.filter { id ->
                    id != userId
                }
            }

        val additionalData = clientService.getUserLightweightInfo(userId, oneToOneUserIds).toSet()
        return chats.map { entity ->
            val chatId = entity.id!!
            val lastMessage = entity.events.firstOrNull { it is MessageEvent } as? MessageEvent
            val message = lastMessage?.let {
                Message(
                    id = it.id,
                    text = it.content,
                    timestamp = it.timestamp,
                )
            }
            when (entity.type) {
                ChatType.ONE_ON_ONE -> {
                    val otherUserId = entity.participants.map { it.id }.first { it != userId }
                    val additional = additionalData.first { it.username == otherUserId }
                    ChatInfoShort(
                        id = chatId,
                        name = additional.name,
                        photo = additional.photoId,
                        type = ChatType.ONE_ON_ONE,
                        lastMessage = message,
                    )
                }

                ChatType.PRIVATE -> {
                    ChatInfoShort(
                        id = chatId,
                        name = entity.name!!,
                        photo = null,
                        type = ChatType.PRIVATE,
                        lastMessage = message,
                    )
                }

                ChatType.MANY -> {
                    ChatInfoShort(
                        id = chatId,
                        name = entity.name!!,
                        photo = null,
                        type = ChatType.MANY,
                        lastMessage = message,
                    )
                }
            }
        }
    }

    fun getUserChat(chatId: String): List<BaseMeta> = chatRepository.findById(chatId).get().events.mapNotNull { event ->
        when (event) {
            is MessageEvent -> {
                val info = clientService.getUserLightweightInfo(event.senderId, listOf(event.senderId)).first()
                event.toMessageMeta(info)
            }

            else -> null
        }
    }

    fun getGroupChatInfo(userId: String, chatId: String): GroupChatInfo {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw IllegalArgumentException("Chat doesn't exist")

        if (userId !in chat.participants.map { it.id }) throw IllegalArgumentException("Chat with user doesn't exist")

        val additionalData = clientService.getUserLightweightInfo(userId, chat.participants.map { it.id }).toSet()
        return GroupChatInfo(
            id = chat.id!!,
            name = chat.name!!,
            description = chat.description,
            type = ChatType.MANY,
            chatPreviewPhotosIds = chat.photos,
            users = chat.participants.map { user ->
                val additional = additionalData.first { user.id == it.username }
                (user as GroupChatUser).toGroupUser(additional.name, additional.photoId)
            },
            mediaContentIds = emptyList(),
        )
    }

    fun updateUserRole(adminId: String, chatId: String, userId: String, updatedUserRole: UpdatedUserRole) {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw IllegalArgumentException("Chat doesn't exist")

        if (adminId !in chat.participants.map { it.id }) throw IllegalArgumentException("Chat with user doesn't exist")

        val chatParticipants = chat.participants.map { it as GroupChatUser }
        if (!chatParticipants.any { it.id == adminId && it.userRole.name == "admin" }) throw IllegalArgumentException("Not admin")

        val user = chatParticipants.find { it.id == userId } ?: throw IllegalStateException("user doesn't exist")

        saveChat(
            chat.copy(
                participants = chatParticipants.map {
                    if (it.id == userId) {
                        GroupChatUser(
                            id = it.id,
                            userRole = UserRole(
                                name = updatedUserRole.name ?: it.userRole.name,
                                canSentMessages = updatedUserRole.canSentMessages ?: it.userRole.canSentMessages,
                                canInviteUsers = updatedUserRole.canInviteUsers ?: it.userRole.canInviteUsers,
                                canPinMessages = updatedUserRole.canPinMessages ?: it.userRole.canPinMessages,
                                canSetRoles = updatedUserRole.canSetRoles ?: it.userRole.canSetRoles,
                                canDeleteUsers = updatedUserRole.canDeleteUsers ?: it.userRole.canDeleteUsers,
                            )
                        )
                    } else {
                        it
                    }
                }
            )
        )
    }

    fun deleteUserFromChat(adminId: String, chatId: String, userId: String) {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw IllegalArgumentException("Chat doesn't exist")

        val admin = chat.participants.first { it.id == adminId } as GroupChatUser
        if (admin.userRole.name != "admin") return

        val deletedUser = userRepository.getByUserId(userId).first()
        deletedUser.chatIds.remove(chat.id)
        userRepository.save(deletedUser)

        chatRepository.save(
            chat.copy(
                participants = chat.participants.filter { it.id != userId }
            )
        )
    }
}
