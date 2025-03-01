package com.point.chats.events.rest

import com.point.chats.chats.services.ChatService
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.events.services.EventsService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class WebSocketChatController(
    private val eventsService: EventsService,
    private val messagingTemplate: SimpMessagingTemplate,
) {

    @MessageMapping("/chat/{chatId}/sendMessage")
    fun sendMessage(@Payload messageRequest: CreateMessageRequest, @DestinationVariable chatId: String) {
        val message = eventsService.createEvent(chatId, messageRequest)
        messagingTemplate.convertAndSend("/topic/chat/$chatId", message)

        messagingTemplate.convertAndSendToUser(
            messageRequest.senderId,
            "/queue/messages",
            message
        )
    }
}
