package com.point.apigateway

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.TokenExpiredException::class)
    fun handleRuntimeException(ex: SecurityException.TokenExpiredException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message)
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleSecurityException(ex: SecurityException): ResponseEntity<ExceptionResponse> {
        val errorResponse = ExceptionResponse("User from token not found")
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(SecurityException.InvalidTokenException::class)
    fun handleSecurityException(ex: SecurityException.InvalidTokenException): ResponseEntity<ExceptionResponse> {
        val errorResponse = ExceptionResponse(ex.message)
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }
}