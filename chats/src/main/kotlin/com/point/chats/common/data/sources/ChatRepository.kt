package com.point.chats.common.data.sources

import com.point.chats.common.data.entities.Chat
import com.point.chats.common.data.entities.UserAuthorities
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, String> {
    fun findChatsByActiveUserAuthorities(activeUserAuthorities: MutableMap<String, UserAuthorities>): MutableList<Chat>
    fun findChatsByUsers(users: MutableSet<String>): MutableList<Chat>
    fun findChatsByUsersContains(users: MutableSet<String>): MutableList<Chat>
}
