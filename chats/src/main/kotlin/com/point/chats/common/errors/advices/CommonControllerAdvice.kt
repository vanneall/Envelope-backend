package com.point.chats.common.errors.advices

import com.point.chats.common.errors.exceptions.ChatNotFoundException
import com.point.chats.common.errors.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonControllerAdvice {

    @ExceptionHandler(ChatNotFoundException::class)
    fun handleChatNotFound(ex: ChatNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("reason" to ex.message.orEmpty()))
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("reason" to ex.message.orEmpty()))
    }
}
