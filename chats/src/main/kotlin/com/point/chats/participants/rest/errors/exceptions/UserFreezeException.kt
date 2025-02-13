package com.point.chats.participants.rest.errors.exceptions

class UserFreezeException(id: String) : RuntimeException("User with id: $id is blocked or freezed")