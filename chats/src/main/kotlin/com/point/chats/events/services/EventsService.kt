package com.point.chats.events.services

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.point.chats.chatsv2.data.entity.event.BaseEvent
import com.point.chats.chatsv2.data.entity.event.MessageSentEvent
import com.point.chats.chatsv2.data.repository.ChatRepositoryV2
import com.point.chats.common.errors.exceptions.ChatNotFoundException
import com.point.chats.common.errors.exceptions.PhotoUploadException
import com.point.chats.common.service.ClientService
import com.point.chats.common.service.PhotoService
import com.point.chats.common.service.UserInfoShortResponse
import com.point.chats.events.data.entities.toMessageEvent
import com.point.chats.events.rest.requests.CreateMessageRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class EventsService(
    private val photoService: PhotoService,
    private val clientService: ClientService,
    private val chatRepository: ChatRepositoryV2,
) {

    fun createEvent(chatId: String, createMessageRequest: CreateMessageRequest): MessageMeta {
        var photos: MutableList<Long>? = null

        if (createMessageRequest.photos != null) {
            val requestPhotos = createMessageRequest.photos
            try {
                photos = MutableList(requestPhotos.size) { index -> photoService.uploadPhoto(requestPhotos[index]).id }

            } catch (e: WebClientResponseException) {
                throw PhotoUploadException(status = e.statusCode, message = e.message)
            }
        }

        val info = clientService.getUserShortInfo(createMessageRequest.senderId)
        val message = createMessageRequest.toMessageEvent(photos)
        return (saveEvent(message, createMessageRequest.senderId, chatId) as MessageSentEvent).toMessageMeta(info)
    }

    fun deleteMessage(chatId: String, messageId: String) {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw ChatNotFoundException(chatId)
        chat.events.removeIf { it.id == messageId }
        chatRepository.save(chat)
    }

    fun saveEvent(event: BaseEvent, eventOwnerId: String, chatId: String): BaseEvent {
        val chat = chatRepository.findById(chatId).getOrNull() ?: throw ChatNotFoundException(chatId)
        chat.events.add(event)
        chatRepository.save(chat)
        return event
    }
}

data class MessageMeta(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("timestamp")
    override val timestamp: Instant,
    @JsonProperty("userName")
    val userName: String,
    @JsonProperty("userPhotoId")
    val userPhotoId: Long?,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("attachments")
    val attachments: List<Long> = emptyList(),
) : BaseMeta

fun MessageSentEvent.toMessageMeta(userInfoShortResponse: UserInfoShortResponse) = MessageMeta(
    id = id,
    timestamp = timestamp,
    userName = userInfoShortResponse.name,
    userPhotoId = userInfoShortResponse.photos.firstOrNull(),
    senderId = senderId,
    text = text,
    attachments = attachments.map { it },
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageMeta::class, name = "message")
)
interface BaseMeta {
    val id: String
    val timestamp: Instant
}