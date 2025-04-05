package com.point.chats.v2.chats.data.repository

import com.point.chats.v2.chats.data.entity.document.ChatDocument
import com.point.chats.v2.chats.data.entity.document.ChatType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepositoryV2 : MongoRepository<ChatDocument, String> {
    fun findByParticipants(participants: List<String>): ChatDocument?
    fun findByIdInAndNameContainingIgnoreCase(chatIds: List<String>, name: String, pageable: Pageable): List<ChatDocument>
    fun deleteByIdInAndType(ids: MutableCollection<String>, type: ChatType)
    fun findByIdIn(ids: MutableCollection<String>, pageable: Pageable): Page<ChatDocument>
}
