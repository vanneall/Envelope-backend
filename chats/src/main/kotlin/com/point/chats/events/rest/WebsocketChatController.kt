package com.point.chats.events.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.chats.chats.services.ChatService
import com.point.chats.events.data.entities.Message
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.events.services.EventsService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.multipart.MultipartFile
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
    ): Message {
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
}

data class CreateMessageRequest2(
    @JsonProperty("content")
    val content: String,
    @JsonProperty("photos")
    val photos: MutableList<Long>? = null,
)
