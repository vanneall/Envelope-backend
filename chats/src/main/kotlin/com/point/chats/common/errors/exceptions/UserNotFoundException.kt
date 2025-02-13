package com.point.chats.common.errors.exceptions

class UserNotFoundException(id: String) : RuntimeException("User with id: $id not found")