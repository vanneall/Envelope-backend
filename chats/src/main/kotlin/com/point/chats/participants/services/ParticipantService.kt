package com.point.chats.participants.services

import com.point.chats.common.data.entities.Chat
import com.point.chats.common.data.entities.updateAndCopy
import com.point.chats.common.data.sources.ChatRepository
import com.point.chats.common.errors.exceptions.UserNotFoundException
import com.point.chats.events.data.entities.Notification
import com.point.chats.events.services.EventsService
import com.point.chats.participants.rest.errors.exceptions.UserNotAdminException
import com.point.chats.participants.rest.requests.InviteParticipantRequest
import com.point.chats.participants.rest.requests.ParticipantUpdateRequest
import org.springframework.stereotype.Service

@Service
class ParticipantService(private val eventsService: EventsService, val chatRepository: ChatRepository) {

    fun updateParticipantPermissions(chatId: String, userId: String, updateRequest: ParticipantUpdateRequest): Chat {
        val chat = chatRepository.findById(chatId).orElseThrow { RuntimeException("Чат не найден") }
        val activeParticipant = chat.activeUserAuthorities.getOrElse(userId) { throw UserNotFoundException(userId) }

        if (!activeParticipant.isAdmin) {
            throw UserNotAdminException(chatId)
        }

        val updatedAuthority = activeParticipant.updateAndCopy(updateRequest)

        chat.activeUserAuthorities[userId] = updatedAuthority
        return chatRepository.save(chat)
    }

    fun inviteParticipant(inviteParticipantRequest: InviteParticipantRequest, chatId: String): String {
        val notification = Notification(text = inviteParticipantRequest.invitedId, type = Notification.Type.INVITE)
        return eventsService.saveEvent(notification, inviteParticipantRequest.inviterId, chatId)
    }
}