package com.point.authorization.rest.errors

import com.point.authorization.data.response.ExceptionResponse
import com.point.authorization.error.exceptions.InvalidTokenException
import com.point.authorization.error.exceptions.NoPasswordMatchesException
import com.point.authorization.error.exceptions.UserAlreadyExists
import com.point.authorization.error.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UserAlreadyExists::class)
    fun handleUserAlreadyExists(ex: UserAlreadyExists): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserAlreadyExists(ex: UserNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleUserAlreadyExists(ex: InvalidTokenException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(NoPasswordMatchesException::class)
    fun handleNoPasswordMatches(ex: NoPasswordMatchesException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}