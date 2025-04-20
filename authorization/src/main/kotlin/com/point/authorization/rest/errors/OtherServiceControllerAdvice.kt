package com.point.authorization.rest.errors

import com.point.authorization.data.response.ExceptionResponse
import com.point.authorization.error.exceptions.OtherServiceException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class OtherServiceControllerAdvice {

    @ExceptionHandler(OtherServiceException::class)
    fun handleUserAlreadyExists(ex: OtherServiceException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, ex.status)
    }
}