package com.point.profiles.requests.rest.errors

import com.point.profiles.errors.ExceptionResponse
import com.point.profiles.requests.errors.exceptions.RequestNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RequestControllerAdvice {

    @ExceptionHandler(RequestNotFoundException::class)
    fun handleInvalidExtension(ex: RequestNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }
}