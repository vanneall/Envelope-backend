package com.point.chats.events.rest

import com.point.chats.common.data.entities.Event
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
        @RequestParam(defaultValue = "10") limit: Int,
    ): List<Event> = eventsService.getChatEvents(chatId, offset, limit)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEvent(@PathVariable chatId: String, @ModelAttribute message: CreateMessageRequest): MessageCreatedResponse =
        MessageCreatedResponse(eventsService.createEvent(chatId, message))

    @PostMapping("/pin/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    fun pinMessage(
        @PathVariable chatId: String,
        @PathVariable messageId: String,
        @RequestParam userId: String
    ) {
        eventsService.pinMessage(chatId, messageId, userId)
    }
}