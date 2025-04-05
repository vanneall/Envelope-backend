package com.point.chats.v2.chats.data.repository

import com.point.chats.v2.chats.data.entity.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<com.point.chats.v2.chats.data.entity.document.UserDocument, String> {
    fun deleteUserDocumentByUserId(userId: String)
    fun getByUserId(userId: String): MutableList<UserDocument>

    fun countByUserIdIn(userIds: MutableCollection<String>): Long
}