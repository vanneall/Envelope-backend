package com.point.chats.participants.rest.errors.exceptions

class UserInviteDeniedException(id: String = ""): RuntimeException("User with id '$id' can't invite other")