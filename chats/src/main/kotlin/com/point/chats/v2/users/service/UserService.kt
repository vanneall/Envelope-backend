package com.point.chats.v2.users.service

import com.point.chats.v2.chats.data.entity.document.ChatDocument
import com.point.chats.v2.chats.data.entity.document.ChatType
import com.point.chats.v2.chats.data.entity.document.UserDocument
import com.point.chats.v2.chats.data.entity.document.addChat
import com.point.chats.v2.chats.data.repository.ChatRepositoryV2
import com.point.chats.v2.chats.data.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val chatsRepository: ChatRepositoryV2) {

    fun createUser(username: String) {
        if (userRepository.existsById(username)) {
            throw IllegalStateException("User already exists")
        }

        var personalChat = ChatDocument(
            type = ChatType.PRIVATE,
            participants = listOf(username),
            name = ChatType.PRIVATE.name,
            description = null,
        )

        personalChat = chatsRepository.save(personalChat)

        val userDocument = UserDocument(userId = username)
        userDocument.addChat(personalChat.id!!)
        userRepository.save(userDocument)
    }

    fun deleteUser(username: String) {
        val userChats = userRepository.getByUserId(username).first().chatIds
        chatsRepository.deleteByIdInAndType(ids = userChats, type = ChatType.ONE_ON_ONE)
        userRepository.deleteUserDocumentByUserId(userId = username)
    }
}