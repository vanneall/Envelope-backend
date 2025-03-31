package com.point.chats.chatsv2.data.entity.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user_chats")
data class UserDocument(
    @Id
    val userId: String,
    val chatIds: MutableList<String> = mutableListOf(),
) {
    fun addChat(chatId: String) {
        if (!chatIds.contains(chatId)) {
            chatIds.add(chatId)
        }
    }
}
