package com.point.chats.v2.chats.data.entity.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user_chats")
data class UserDocument(
    @Id
    val userId: String,
    val chatIds: MutableList<String> = mutableListOf(),
)

fun UserDocument.addChat(chatId: String) {
    if (!chatIds.contains(chatId)) {
        chatIds.add(chatId)
    }
}
