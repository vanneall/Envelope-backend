package com.point.chats.v2.chats.rest


import com.point.chats.events.data.rest.meta.BaseMeta
import com.point.chats.v2.chats.rest.requests.CreateChatRequest
import com.point.chats.v2.chats.rest.response.ChatIdResponse
import com.point.chats.v2.chats.rest.response.ChatInfoShortResponse
import com.point.chats.v2.chats.rest.response.toResponse
import com.point.chats.v2.chats.service.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats/api-v2")
class ChatsController(private val chatService: ChatService) {

    @PostMapping
    fun createChat(
        @RequestHeader(USER_ID) userId: String,
        @RequestBody request: CreateChatRequest,
    ): ResponseEntity<ChatIdResponse> {
        val chatId = chatService.createChat(userId, request.participantIds, request.name)
        return ResponseEntity.ok(ChatIdResponse(chatId))
    }

    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @RequestHeader(USER_ID) userId: String,
        @PathVariable chatId: String
    ): ResponseEntity<Unit> {
        chatService.deleteChat(userId, chatId)
        return ResponseEntity.ok(Unit)
    }

    @GetMapping
    fun getUserChats(
        @RequestHeader(USER_ID) userId: String,
        @RequestParam(defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<ChatInfoShortResponse>> {
        val chats = chatService.getUserChats(userId, name, limit, offset).map {
            it.toResponse()
        }
        return ResponseEntity.ok(chats)
    }

    @GetMapping("/{chatId}")
    fun getChatEventsById(
        @RequestHeader(USER_ID) userId: String,
        @PathVariable("chatId") chatId: String
    ): ResponseEntity<List<BaseMeta>> {
        val chats = chatService.getUserChat(chatId)
        return ResponseEntity.ok(chats)
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    fun test() {
    }

    private companion object {
        const val USER_ID = "X-User-ID"
    }
}
