package com.point.chats.events.rest

import com.point.chats.chatsv2.data.entity.event.BaseEvent
import com.point.chats.events.rest.requests.CreateMessageRequest
import com.point.chats.events.rest.responses.MessageCreatedResponse
import com.point.chats.events.services.EventsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats/{chatId}/events")
class EventRestController(private val eventsService: EventsService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun fetchEvents(
        @PathVariable chatId: String,
        @RequestParam(defaultValue = "0") offset: Int,
        @RequestParam(defaultValue = "35") limit: Int,
    ): List<BaseEvent> = eventsService.getChatEvents(chatId, offset, limit)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEvent(@PathVariable chatId: String, @ModelAttribute message: CreateMessageRequest): MessageCreatedResponse =
        MessageCreatedResponse(eventsService.createEvent(chatId, message).id)

    @PostMapping("/pin/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    fun pinMessage(
        @PathVariable chatId: String,
        @PathVariable messageId: String,
        @RequestHeader("X-User-ID") userId: String,
    ) {
//        eventsService.pinMessage(chatId, messageId, userId)
    }
}