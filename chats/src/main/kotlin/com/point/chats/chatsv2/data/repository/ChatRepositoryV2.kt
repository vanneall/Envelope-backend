package com.point.chats.chatsv2.data.repository

import com.point.chats.chatsv2.data.entity.document.ChatDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepositoryV2 : MongoRepository<ChatDocument, String> {
    fun findByParticipants(participants: List<String>): ChatDocument?
    fun findByIdInAndNameContainingIgnoreCase(chatIds: List<String>, name: String, pageable: Pageable): List<ChatDocument>
    fun findAllByIdAndNameContainsIgnoreCase(id: List<String>, name: String, pageable: Pageable): Page<ChatDocument>
    fun findChatDocumentById(id: String): MutableList<ChatDocument>
}
