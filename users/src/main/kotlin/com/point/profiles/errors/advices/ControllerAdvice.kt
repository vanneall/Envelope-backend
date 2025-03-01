package com.point.profiles.errors.advices

import com.point.profiles.errors.exceptions.PhotoUploadException
import com.point.profiles.errors.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse("USER_NOT_FOUND", ex.message ?: "User not found"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("INTERNAL_ERROR", ex.message ?: "Generic error"))
    }

    @ExceptionHandler(PhotoUploadException::class)
    fun handleGenericException(ex: PhotoUploadException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(ex.status)
            .body(ErrorResponse(ex.status.value().toString(), ex.message ?: "Photo upload error"))
    }

    data class ErrorResponse(
        val errorCode: String,
        val errorMessage: String
    )
}
