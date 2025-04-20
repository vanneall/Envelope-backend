package com.point.authorization.rest.errors

import com.point.authorization.data.response.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ArgumentsControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalidArg(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val response = ex.bindingResult
            .fieldErrors
            .map { ExceptionResponse(code = "${it.field.uppercase()}:${it.defaultMessage!!}") }
            .first()

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}