package com.point.chats.participants.rest.errors.advices

import com.point.chats.participants.rest.errors.exceptions.UserFreezeException
import com.point.chats.participants.rest.errors.exceptions.UserInviteDeniedException
import com.point.chats.participants.rest.errors.exceptions.UserNotAdminException
import com.point.chats.participants.rest.errors.exceptions.UserWriteMessageDeniedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ParticipantControllerAdvice {

    @ExceptionHandler(UserInviteDeniedException::class)
    fun handleUserInviteException(ex: UserInviteDeniedException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("reason" to ex.message.orEmpty()))
    }

    @ExceptionHandler(UserWriteMessageDeniedException::class)
    fun handleUserWriteMessageException(ex: UserWriteMessageDeniedException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("reason" to ex.message.orEmpty()))
    }

    @ExceptionHandler(UserFreezeException::class)
    fun handleUserUserFreezeException(ex: UserFreezeException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("reason" to ex.message.orEmpty()))
    }

    @ExceptionHandler(UserNotAdminException::class)
    fun handleUserNotAdminException(ex: UserNotAdminException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("reason" to ex.message.orEmpty()))
    }
}