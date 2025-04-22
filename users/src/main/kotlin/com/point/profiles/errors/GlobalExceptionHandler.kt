package com.point.profiles.errors

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserException::class)
    fun handleUserException(ex: UserException): ResponseEntity<ErrorResponse> {
        val status = when (ex.errorCode) {
            ErrorCodes.USERNAME_EMPTY,
            ErrorCodes.NAME_EMPTY,
            ErrorCodes.BIRTHDATE_INVALID -> HttpStatus.BAD_REQUEST

            ErrorCodes.USER_BLOCKED,
            ErrorCodes.USER_ALREADY_FRIEND,
            ErrorCodes.USER_ALREADY_EXISTS,
            ErrorCodes.INVALID_BLOCK_MYSELF,
            ErrorCodes.REQUEST_ALREADY_SENT -> HttpStatus.CONFLICT

            ErrorCodes.USER_NOT_FOUND,
            ErrorCodes.REQUEST_NOT_FOUND -> HttpStatus.NOT_FOUND

            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val responseHeaders = HttpHeaders().apply {
            add("X-Error-Code", ex.errorCode.name)
            add("Content-Type", "application/json")
        }

        return ResponseEntity.status(status)
            .headers(responseHeaders)
            .body(ErrorResponse(ex.errorCode.name))
    }
}

