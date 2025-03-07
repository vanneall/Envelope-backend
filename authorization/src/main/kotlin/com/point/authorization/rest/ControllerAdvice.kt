package com.point.authorization.rest

import com.point.authorization.data.response.ExceptionResponse
import com.point.authorization.utils.InvalidUserAuthorizationCredentials
import com.point.authorization.utils.InvalidUserRegistrationCredentials
import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.HttpStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserRegistrationCredentials::class)
    fun handleRuntimeException(ex: InvalidUserRegistrationCredentials): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message)
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(InvalidUserAuthorizationCredentials::class)
    fun handleRuntimeException(ex: InvalidUserAuthorizationCredentials) : ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message)
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleRuntimeException(ex: ConstraintViolationException) : ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message)
        return ResponseEntity(response, HttpStatus.CONFLICT)
    }
}