package com.point.chats.chats.rest

import com.point.chats.common.data.entities.Chat
import com.point.chats.chats.rest.requests.ChatUpdateRequest
import com.point.chats.chats.rest.requests.CreateChatRequest
import com.point.chats.chats.rest.responses.ChatCreatedResponse
import com.point.chats.chats.services.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats")
class ChatsRestController(private val chatService: ChatService) {

    @GetMapping("/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    fun getChat(@PathVariable chatId: String): Chat = chatService.getChat(chatId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createChat(@RequestBody request: CreateChatRequest): ChatCreatedResponse =
        ChatCreatedResponse(chatService.createChat(request))

    @PatchMapping("/{chatId}")
    fun updateChat(
        @PathVariable chatId: String,
        @RequestParam userId: String,
        @RequestBody updateRequest: ChatUpdateRequest
    ): ResponseEntity<Chat> {
        val updatedChat = chatService.updateChat(chatId, userId, updateRequest)
        return ResponseEntity.ok(updatedChat)
    }

    @DeleteMapping("/{chatId}")
    fun deleteChat(@PathVariable chatId: String) {
        chatService.deleteChat(chatId)
    }
}