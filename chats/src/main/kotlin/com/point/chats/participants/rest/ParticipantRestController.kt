package com.point.chats.participants.rest

import com.point.chats.common.data.entities.Chat
import com.point.chats.participants.rest.requests.ParticipantUpdateRequest
import com.point.chats.participants.rest.requests.InviteParticipantRequest
import com.point.chats.participants.services.ParticipantService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats/{chatId}/participants")
class ParticipantRestController(private val participantService: ParticipantService) {

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateParticipantPermissions(
        @PathVariable chatId: String,
        @RequestHeader("X-User-ID") userId: String,
        @RequestBody updateRequest: ParticipantUpdateRequest,
    ): Chat = participantService.updateParticipantPermissions(chatId, userId, updateRequest)


    @PostMapping("/invite")
    fun inviteParticipant(@PathVariable chatId: String, @RequestBody inviteParticipantRequest: InviteParticipantRequest) {
        participantService.inviteParticipant(inviteParticipantRequest, chatId)
    }
}