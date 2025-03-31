package com.point.chats.events.services

import com.point.chats.chatsv2.data.entity.event.BaseEvent
import com.point.chats.chatsv2.data.entity.event.MessageSentEvent
import com.point.chats.chatsv2.data.repository.ChatRepositoryV2
import com.point.chats.common.errors.exceptions.ChatNotFoundException
import com.point.chats.common.errors.exceptions.PhotoUploadException
import com.point.chats.common.service.PhotoService
import com.point.chats.events.data.entities.toMessageEvent
import com.point.chats.events.rest.requests.CreateMessageRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class EventsService(private val photoService: PhotoService, private val chatRepository: ChatRepositoryV2) {

    fun getChatEvents(chatId: String, page: Int, size: Int): List<BaseEvent> = chatRepository.findById(chatId)
        .getOrElse { throw ChatNotFoundException(chatId) }
        .events
        .reversed()
        .drop(page)
        .take(size)

    fun createEvent(chatId: String, createMessageRequest: CreateMessageRequest): MessageSentEvent {
        var photos: MutableList<Long>? = null

        if (createMessageRequest.photos != null) {
            val requestPhotos = createMessageRequest.photos
            try {
                photos = MutableList(requestPhotos.size) { index -> photoService.uploadPhoto(requestPhotos[index]).id }

            } catch (e: WebClientResponseException) {
                throw PhotoUploadException(status = e.statusCode, message = e.message)
            }
        }

        val message = createMessageRequest.toMessageEvent(photos)
        return saveEvent(message, createMessageRequest.senderId, chatId) as MessageSentEvent
    }

    fun saveEvent(event: BaseEvent, eventOwnerId: String, chatId: String): BaseEvent {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw ChatNotFoundException(chatId)
        chat.events.add(event)
        chatRepository.save(chat)
        return event
    }
}