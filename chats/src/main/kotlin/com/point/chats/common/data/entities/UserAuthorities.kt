package com.point.chats.common.data.entities

import com.point.chats.participants.rest.requests.ParticipantUpdateRequest

data class UserAuthorities(
    val isWriteEnable: Boolean = true,
    val isInviteEnable: Boolean = false,
    val isKickEnable: Boolean = false,
    val isPinMessageEnable: Boolean = false,
    val isBlockUserEnable: Boolean = false,
    val isAdmin: Boolean = false,
)

fun UserAuthorities.updateAndCopy(newAuthorities: ParticipantUpdateRequest) = copy(
    isWriteEnable = newAuthorities.isWriteEnable ?: isWriteEnable,
    isInviteEnable = newAuthorities.isInviteEnable ?: isInviteEnable,
    isKickEnable = newAuthorities.isKickEnable ?: isKickEnable,
    isPinMessageEnable = newAuthorities.isPinMessageEnable ?: isPinMessageEnable,
    isBlockUserEnable = newAuthorities.isBlockUserEnable ?: isBlockUserEnable
)
