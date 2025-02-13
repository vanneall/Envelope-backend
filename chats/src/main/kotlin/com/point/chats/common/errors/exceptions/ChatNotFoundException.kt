package com.point.chats.common.errors.exceptions

class ChatNotFoundException(id: String) : RuntimeException("Chat with id $id not found")
