package com.point.chats.events.rest

import com.point.chats.events.data.rest.commands.*
import com.point.chats.events.data.rest.meta.*
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.events.services.EventsService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class WebSocketChatController(private val eventsService: EventsService) {

    @MessageMapping("/chat/{chatId}/sendMessage")
    @SendTo("/topic/chat/{chatId}")
    fun sendMessage(
        @Payload messageRequest: CreateMessageRequest2,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageMeta = eventsService.createMessage(
        chatId = chatId,
        createMessageRequest = CreateMessageRequest(
            senderId = principal.name,
            content = messageRequest.content,
            photos = messageRequest.photos ?: listOf(),
        )
    )

    @MessageMapping("/chat/{chatId}/deleteMessage")
    @SendTo("/topic/chat/{chatId}")
    fun deleteMessage(
        @Payload deleteMessage: DeleteMessage,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageDeletedMeta {
        eventsService.deleteMessage(chatId, deleteMessage.messageId)
        return MessageDeletedMeta(deletedMessageId = deleteMessage.messageId)
    }

    @MessageMapping("/chat/{chatId}/editMessage")
    @SendTo("/topic/chat/{chatId}")
    fun editMessage(
        @Payload editMessage: EditMessage,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageEditedMeta {
        eventsService.editMessage(chatId, editMessage)
        return MessageEditedMeta(editedMessageId = editMessage.messageId, newContent = editMessage.content)
    }

    @MessageMapping("/chat/{chatId}/pinMessage")
    @SendTo("/topic/chat/{chatId}")
    fun pinMessage(
        @Payload pinMessage: PinMessage,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessagePinnedMeta {
        return eventsService.pinMessage(chatId, pinMessage.messageId)
    }

    @MessageMapping("/chat/{chatId}/unpinMessage")
    @SendTo("/topic/chat/{chatId}")
    fun unpinMessage(
        @Payload unpinMessage: UnpinMessage,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageUnpinnedMeta {
        eventsService.unpinMessage(chatId, unpinMessage.messageId)
        return MessageUnpinnedMeta(messageId = unpinMessage.messageId)
    }

    @MessageMapping("/chat/{chatId}/typingStart")
    @SendTo("/topic/chat/{chatId}")
    fun typingStarted(
        @Payload userId: UserId,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageTypedMeta {
        val name = eventsService.getTypingUserName(userId.username)
        return MessageTypedMeta(username = userId.username, name = name)
    }

    @MessageMapping("/chat/{chatId}/typingEnd")
    @SendTo("/topic/chat/{chatId}")
    fun typingEnded(
        @Payload userId: UserId,
        @DestinationVariable chatId: String,
        principal: Principal,
    ): MessageStopTypedMeta {
        val name = eventsService.getTypingUserName(userId.username)
        return MessageStopTypedMeta(username = userId.username, name = name)
    }
}
