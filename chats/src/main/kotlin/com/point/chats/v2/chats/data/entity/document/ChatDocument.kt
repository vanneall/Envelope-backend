package com.point.chats.v2.chats.data.entity.document

import com.point.chats.events.data.persistance.entities.BaseEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chats")
data class ChatDocument(
    @Id
    val id: String? = null,
    val type: ChatType,
    val participants: List<String>,
    val name: String? = null,
    val description: String? = null,
    val photos: List<Long> = emptyList(),
    val events: MutableList<BaseEvent> = mutableListOf(),
    val pinnedMessage: BaseEvent? = null,
)

enum class ChatType {
    PRIVATE,
    ONE_ON_ONE,
    ;
}