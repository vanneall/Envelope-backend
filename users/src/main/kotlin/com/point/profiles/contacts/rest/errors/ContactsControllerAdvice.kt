package com.point.profiles.contacts.rest.errors

import com.point.profiles.contacts.errors.exceptions.SelfDeleteException
import com.point.profiles.contacts.errors.exceptions.UserNotInContactsException
import com.point.profiles.errors.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ContactsControllerAdvice {

    @ExceptionHandler(SelfDeleteException::class)
    fun handleSelfDelete(ex: SelfDeleteException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotInContactsException::class)
    fun handleUserNotInContacts(ex: UserNotInContactsException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }
}