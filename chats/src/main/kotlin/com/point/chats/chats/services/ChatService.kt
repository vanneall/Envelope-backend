package com.point.chats.chats.services

import com.point.chats.chats.rest.requests.ChatUpdateRequest
import com.point.chats.chats.rest.requests.CreateChatRequest
import com.point.chats.common.data.entities.Chat
import com.point.chats.common.data.entities.toChat
import com.point.chats.common.data.entities.update
import com.point.chats.common.data.sources.ChatRepository
import com.point.chats.common.errors.exceptions.ChatNotFoundException
import com.point.chats.common.errors.exceptions.UserNotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatService(private val chatRepository: ChatRepository) {

    fun getChat(chatId: String): Chat = chatRepository.findById(chatId)
        .orElseThrow { throw ChatNotFoundException(chatId) }

    fun createChat(request: CreateChatRequest): String = requireNotNull(chatRepository.save(request.toChat()).id)

    fun updateChat(chatId: String, userId: String, updateRequest: ChatUpdateRequest): Chat {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw ChatNotFoundException(chatId)
        chat.activeUserAuthorities[userId] ?: throw UserNotFoundException(userId)

        chat.update(newChat = updateRequest)

        return chatRepository.save(chat)
    }

    fun deleteChat(chatId: String) {
        chatRepository.deleteById(chatId)
    }
}

