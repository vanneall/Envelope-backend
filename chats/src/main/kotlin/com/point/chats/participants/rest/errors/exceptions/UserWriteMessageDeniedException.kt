package com.point.chats.participants.rest.errors.exceptions

class UserWriteMessageDeniedException(id: String = ""): RuntimeException("User with id '$id' can't send messages")