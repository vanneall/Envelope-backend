package com.point.photo.rest.errors

import com.point.photo.errors.exceptions.FileNotFoundException
import com.point.photo.errors.exceptions.FileValidationException
import com.point.photo.rest.responses.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class FileControllerAdvice {

    @ExceptionHandler(FileValidationException::class)
    fun handleInvalidExtension(ex: FileValidationException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(FileNotFoundException::class)
    fun handleFileValidation(ex: FileNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(requireNotNull(ex.message))
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }
}