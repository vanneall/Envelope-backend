package com.point.chats.participants.rest.requests

data class ParticipantUpdateRequest(
    val isWriteEnable: Boolean? = null,
    val isInviteEnable: Boolean? = null,
    val isKickEnable: Boolean? = null,
    val isPinMessageEnable: Boolean? = null,
    val isBlockUserEnable: Boolean? = null
)
