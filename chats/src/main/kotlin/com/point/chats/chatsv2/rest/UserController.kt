package com.point.chats.chatsv2.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.chats.chatsv2.data.entity.document.ChatDocument
import com.point.chats.chatsv2.data.entity.document.ChatType
import com.point.chats.chatsv2.data.entity.document.UserDocument
import com.point.chats.chatsv2.data.entity.event.BaseEvent
import com.point.chats.chatsv2.data.repository.ChatRepositoryV2
import com.point.chats.chatsv2.data.repository.UserRepository
import com.point.chats.chatsv2.service.ChatService2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats/api-v2")
class UserController(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepositoryV2,
    private val chatService: ChatService2,
) {

    @GetMapping("/test")
    fun test() = "test"

    @PostMapping("/users")
    fun createUser(@RequestHeader(USER_ID) userId: String): ResponseEntity<String> {

        if (userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("Пользователь уже существует")
        }

        var personalChat = ChatDocument(
            type = ChatType.PRIVATE,
            participants = listOf(userId),
            name = "Личный чат",
            description = "Это ваш личный чат"
        )

        personalChat = chatRepository.save(personalChat)

        val userDocument = UserDocument(userId = userId)
        userDocument.addChat(personalChat.id!!)
        userRepository.save(userDocument)

        return ResponseEntity.ok(userId)
    }

    @PostMapping
    fun createChat(
        @RequestHeader("X-User-ID") userId: String,
        @RequestBody request: CreateChatRequest,
    ): ResponseEntity<ChatIdResponse> {
        val chatId = chatService.createChat(userId, request.participantIds, request.name)
        return ResponseEntity.ok(ChatIdResponse(chatId))
    }

    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @RequestHeader(USER_ID) userId: String,
        @PathVariable chatId: String
    ): ResponseEntity<String> {
        chatService.deleteChat(userId, chatId)
        return ResponseEntity.ok("Чат успешно удален")
    }

    @GetMapping
    fun getUserChats(
        @RequestHeader("X-User-ID") userId: String,
        @RequestParam(defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<ChatInfoShortResponse>> {
        val chats = chatService.getUserChats(userId, name, limit, offset).map {
            ChatInfoShortResponse(
                id = it.id!!,
                name = it.name!!,
                lastMessage = ""
            )
        }
        return ResponseEntity.ok(chats)
    }

    @GetMapping("/{chatId}")
    fun getChaById(
        @RequestHeader("X-User-ID") userId: String,
        @PathVariable("chatId") chatId: String
    ): ResponseEntity<MutableList<BaseEvent>> {
        val chats = chatService.getUserChat(chatId)
        chats.sortByDescending { it.timestamp }
        return ResponseEntity.ok(chats)
    }

    private companion object {
        const val USER_ID = "X-User-ID"
    }
}

data class ChatInfoShortResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("last_message")
    val lastMessage: String? = null,
)

data class CreateChatRequest(
    @JsonProperty("participants")
    val participantIds: List<String>,
    @JsonProperty("name")
    val name: String? = null,
)

data class ChatIdResponse(
    @JsonProperty("id")
    val id: String,
)
