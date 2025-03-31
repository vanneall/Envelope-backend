package com.point.chats.events.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.chats.chatsv2.data.entity.event.DeleteMessageEvent
import com.point.chats.chatsv2.data.entity.event.MessageSentEvent
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.events.services.EventsService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class WebSocketChatController(
    private val eventsService: EventsService,
) {

    @MessageMapping("/chat/{chatId}/sendMessage")
    @SendTo("/topic/chat/{chatId}")
    fun sendMessage(
        @Payload messageRequest: CreateMessageRequest2,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageSentEvent {
        val message = eventsService.createEvent(
            chatId,
            CreateMessageRequest(
                senderId = principal.name,
                content = messageRequest.content,
                photos = mutableListOf(),
            )
        )
        return message
    }

    @MessageMapping("/chat/{chatId}/deleteMessage")
    @SendTo("/topic/chat/{chatId}")
    fun deleteMessage(
        @Payload deleteMessage: DeleteMessage,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): DeleteMessageEvent {
        eventsService.deleteMessage(
            chatId,
            deleteMessage.messageId,
        )
        return DeleteMessageEvent(messageId = deleteMessage.messageId)
    }
}

data class CreateMessageRequest2(
    @JsonProperty("content")
    val content: String,
    @JsonProperty("photos")
    val photos: MutableList<Long>? = null,
)

data class DeleteMessage(
    @JsonProperty("message_id")
    val messageId: String
)
