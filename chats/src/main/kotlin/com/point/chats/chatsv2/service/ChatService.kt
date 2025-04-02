package com.point.chats.chatsv2.service

import com.point.chats.chatsv2.data.entity.document.ChatDocument
import com.point.chats.chatsv2.data.entity.document.ChatType
import com.point.chats.chatsv2.data.entity.document.UserDocument
import com.point.chats.chatsv2.data.entity.event.MessageSentEvent
import com.point.chats.chatsv2.data.repository.ChatRepositoryV2
import com.point.chats.chatsv2.data.repository.UserRepository
import com.point.chats.common.service.ClientService
import com.point.chats.events.services.BaseMeta
import com.point.chats.events.services.toMessageMeta
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ChatService2(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepositoryV2,
    private val clientService: ClientService,
) {

    fun createChat(userId: String, participantIds: List<String>, name: String?): String {
        val allParticipants = (listOf(userId) + participantIds).sorted()
        validateUsersExist(allParticipants)

        val existingChat = chatRepository.findByParticipants(allParticipants)
        if (existingChat != null) {
            return existingChat.id!!
        }

        val newChat = ChatDocument(
            type = ChatType.ONE_ON_ONE,
            participants = allParticipants,
            name = name ?: "$userId-${participantIds.first()}"
        )
        return saveChat(newChat)
    }

    private fun validateUsersExist(userIds: List<String>) {
        val existingUsers = userRepository.findAllById(userIds).map { it.userId }.toSet()
        val missingUsers = userIds.filterNot { it in existingUsers }

        require(missingUsers.isEmpty()) { "Пользователи не найдены: $missingUsers" }
    }

    fun deleteChat(userId: String, chatId: String) {
        val chat = chatRepository.findById(chatId).orElseThrow {
            IllegalArgumentException("Чат не найден")
        }

        require(userId in chat.participants) { "Вы не являетесь участником этого чата" }

        chat.participants.forEach { participantId ->
            val userDoc = userRepository.findById(participantId).orElse(null) ?: return@forEach
            userDoc.chatIds.remove(chatId)
            userRepository.save(userDoc)
        }

        chatRepository.deleteById(chatId)
    }

    private fun saveChat(chat: ChatDocument): String {
        val savedChat = chatRepository.save(chat)
        savedChat.participants.forEach { userId ->
            val userDoc = userRepository.findById(userId).orElse(UserDocument(userId))
            userDoc.addChat(savedChat.id!!)
            userRepository.save(userDoc)
        }

        return savedChat.id!!
    }

    fun getUserChats(userId: String, name: String, limit: Int, offset: Int): List<ChatDocument> {
        val userDoc = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("Пользователь не найден")
        }

        val pageable = PageRequest.of(offset, limit)
        return chatRepository.findByIdInAndNameContainingIgnoreCase(userDoc.chatIds, name, pageable)
    }

    fun getUserChat(chatId: String): List<BaseMeta> = chatRepository.findById(chatId).get().events.mapNotNull { event ->
        when (event) {
            is MessageSentEvent -> {
                val info = clientService.getUserShortInfo(event.senderId)
                event.toMessageMeta(info)
            }
            else -> null
        }
    }
}
