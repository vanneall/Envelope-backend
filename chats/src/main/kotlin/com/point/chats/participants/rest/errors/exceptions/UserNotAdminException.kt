package com.point.chats.participants.rest.errors.exceptions

class UserNotAdminException(id: String) : RuntimeException("User with id: $id is not admin")