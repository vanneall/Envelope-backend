package com.point.chats.common.data.sources

import com.point.chats.common.data.entities.Chat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, String>
