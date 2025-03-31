package com.point.chats.chatsv2.data.repository

import com.point.chats.chatsv2.data.entity.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<UserDocument, String>