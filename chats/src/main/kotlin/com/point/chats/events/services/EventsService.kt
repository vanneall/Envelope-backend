package com.point.chats.events.services

import com.point.chats.common.data.entities.*
import com.point.chats.common.data.sources.ChatRepository
import com.point.chats.common.errors.exceptions.ChatNotFoundException
import com.point.chats.common.errors.exceptions.PhotoUploadException
import com.point.chats.common.errors.exceptions.UserNotFoundException
import com.point.chats.common.service.PhotoService
import com.point.chats.events.data.entities.Message
import com.point.chats.events.data.entities.Notification
import com.point.chats.events.data.entities.toMessage
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.participants.rest.errors.exceptions.UserInviteDeniedException
import com.point.chats.participants.rest.errors.exceptions.UserWriteMessageDeniedException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class EventsService(private val photoService: PhotoService, private val chatRepository: ChatRepository) {

    fun getChatEvents(chatId: String, page: Int, size: Int): List<Event> = chatRepository.findById(chatId)
        .getOrElse { throw ChatNotFoundException(chatId) }
        .events
        .reversed()
        .drop(page)
        .take(size)

    fun createEvent(chatId: String, createMessageRequest: CreateMessageRequest): Message {
        var photos: MutableList<Long>? = null

        if (createMessageRequest.photos != null) {
            val requestPhotos = createMessageRequest.photos
            try {
                photos = MutableList(requestPhotos.size) { index -> photoService.uploadPhoto(requestPhotos[index]).id }

            } catch (e: WebClientResponseException) {
                throw PhotoUploadException(status = e.statusCode, message = e.message)
            }
        }

        val message = createMessageRequest.toMessage(photos)
        return saveEvent(message, message.senderId, chatId) as Message
    }

    fun saveEvent(event: Event, eventOwnerId: String, chatId: String): Event {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw ChatNotFoundException(chatId)

        val userAuthorities = chat.requireUserAuthorities(eventOwnerId)

        when (event) {
            is Notification -> {
                if (!userAuthorities.isInviteEnable) throw UserInviteDeniedException(eventOwnerId)
                chat.addActiveParticipant(event.id)
            }

            is Message -> {
                if (!userAuthorities.isWriteEnable) throw UserWriteMessageDeniedException(eventOwnerId)
            }
        }

        chat.events.add(event)
        chatRepository.save(chat)
        return event
    }

    fun pinMessage(chatId: String, messageId: String, userId: String): Chat {
        val chat = chatRepository.findById(chatId).orElseThrow { ChatNotFoundException("chatId") }
        val participant = chat.activeUserAuthorities[userId] ?: throw UserNotFoundException(userId)

        require(participant.isPinMessageEnable) {
            throw RuntimeException("У пользователя нет прав на закрепление сообщений")
        }

        val notification = Notification(type = Notification.Type.PIN_MESSAGE)

        chat.pinnedMessage = chat.events.requiredMessageById(messageId).content
        chat.events.add(notification)
        return chatRepository.save(chat)
    }
}