package com.point.chats.chats.services

import com.point.chats.chats.rest.requests.ChatUpdateRequest
import com.point.chats.chats.rest.requests.CreateChatRequest
import com.point.chats.common.data.entities.Chat
import com.point.chats.common.data.entities.ChatInfoResponse
import com.point.chats.common.data.entities.toChat
import com.point.chats.common.data.entities.update
import com.point.chats.common.data.sources.ChatRepository
import com.point.chats.common.errors.exceptions.ChatNotFoundException
import com.point.chats.common.errors.exceptions.UserNotFoundException
import com.point.chats.common.service.PhotoService
import com.point.chats.events.data.entities.Message
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatService(private val photoService: PhotoService, private val chatRepository: ChatRepository) {

    fun getChats(userId: String, limit: Int, offset: Int): List<ChatInfoResponse> =
        chatRepository.findChatsByUsersContains(mutableSetOf(userId)).drop(offset).take(limit).map {
            ChatInfoResponse(
                id = it.id,
                name = it.name,
                lastMessage = it.events.firstOrNull { it is Message }?.let { (it as Message).content }
            )
        }

    fun getChat(chatId: String): Chat = chatRepository.findById(chatId)
        .orElseThrow { throw ChatNotFoundException(chatId) }

    fun createChat(request: CreateChatRequest): String {
        val photoId = request.photo?.let {
            mutableListOf(photoService.uploadPhoto(it).id)
        } ?: mutableListOf()
        return requireNotNull(chatRepository.save(request.toChat(photoId)).id)
    }

    fun updateChat(chatId: String, userId: String, updateRequest: ChatUpdateRequest): Chat {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw ChatNotFoundException(chatId)
        chat.activeUserAuthorities[userId] ?: throw UserNotFoundException(userId)
        val photoId = updateRequest.photo?.let { photoService.uploadPhoto(it).id }

        chat.update(newChat = updateRequest, photoId)

        return chatRepository.save(chat)
    }

    fun deleteChat(chatId: String) {
        chatRepository.deleteById(chatId)
    }
}

