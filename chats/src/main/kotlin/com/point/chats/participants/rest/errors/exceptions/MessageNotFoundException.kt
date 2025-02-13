package com.point.chats.participants.rest.errors.exceptions

class MessageNotFoundException(id: String) : RuntimeException("Message with id $id not found")