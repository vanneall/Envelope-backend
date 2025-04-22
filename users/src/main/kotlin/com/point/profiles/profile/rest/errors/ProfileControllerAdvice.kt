package com.point.profiles.profile.rest.errors

import com.point.profiles.errors.ExceptionResponse
import com.point.profiles.profile.errors.exceptions.MediaServiceUnknownException
import com.point.profiles.profile.errors.exceptions.SaveProfileException
import com.point.profiles.profile.errors.exceptions.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ProfileControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(ex: UserAlreadyExistsException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(SaveProfileException::class)
    fun handleUserAlreadyExists(ex: SaveProfileException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MediaServiceUnknownException::class)
    fun handleUserAlreadyExists(ex: MediaServiceUnknownException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, ex.statusCode)
    }
}