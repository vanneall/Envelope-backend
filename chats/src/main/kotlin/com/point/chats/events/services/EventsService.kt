package com.point.chats.events.services

import com.point.chats.common.service.ClientService
import com.point.chats.common.service.PhotoService
import com.point.chats.events.data.persistance.entities.MessageEvent
import com.point.chats.events.data.persistance.entities.toMessageMeta
import com.point.chats.events.data.rest.commands.EditMessage
import com.point.chats.events.data.rest.meta.MessageMeta
import com.point.chats.events.data.rest.meta.MessagePinnedMeta
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.v2.chats.data.repository.ChatRepositoryV2
import org.springframework.stereotype.Service

@Service
class EventsService(
    private val photoService: PhotoService,
    private val clientService: ClientService,
    private val chatRepository: ChatRepositoryV2,
) {

    fun createMessage(chatId: String, createMessageRequest: CreateMessageRequest): MessageMeta {
        val chat = chatRepository.findById(chatId).get()
        val senderId = createMessageRequest.senderId
        val messageEvent = MessageEvent(
            senderId = senderId,
            content = createMessageRequest.content,
            attachments = createMessageRequest.photos,
            isEdited = false,
            isPinned = false,
        )
        chat.events.addFirst(messageEvent)
        chatRepository.save(chat)

        val lightweightInfoResponse =
            clientService.getUserLightweightInfo(username = senderId, ids = listOf(senderId)).first()
        return messageEvent.toMessageMeta(lightweightInfoResponse)
    }

    fun deleteMessage(chatId: String, messageId: String) {
        val chat = chatRepository.findById(chatId).get()
        chat.events.removeIf { it.id == messageId }
        chatRepository.save(chat)
    }

    fun editMessage(chatId: String, editMessage: EditMessage) {
        val chat = chatRepository.findById(chatId).get()
        val events = chat.events
            .filter { it.id == editMessage.messageId }
            .map { (it as MessageEvent).copy(isEdited = true, content = editMessage.content) }
        chatRepository.save(chat.copy(events = events.toMutableList()))
    }

    fun pinMessage(chatId: String, messageId: String): MessagePinnedMeta {
        var chat = chatRepository.findById(chatId).get()
        val message = chat.events.first { it.id == messageId }
        chat = chat.copy(pinnedMessage = message)
        chatRepository.save(chat)
        return MessagePinnedMeta(
            messageId = messageId,
            content = (message as MessageEvent).content
        )
    }

    fun unpinMessage(chatId: String, messageId: String) {
        var chat = chatRepository.findById(chatId).get()
        chat = chat.copy(pinnedMessage = null)
        chatRepository.save(chat)
    }

    fun getTypingUserName(username: String): String {
        return clientService.getUserLightweightInfo(username, listOf(username)).first().name
    }
}