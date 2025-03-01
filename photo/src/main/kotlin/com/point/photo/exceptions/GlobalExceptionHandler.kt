package com.point.photo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileExtensionException::class)
    fun handleInvalidExtension(ex: InvalidFileExtensionException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "Invalid file extension")))
    }

    @ExceptionHandler(FileSizeExceededException::class)
    fun handleFileSizeExceeded(ex: FileSizeExceededException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "File size exceeds allowed limit")))
    }

    @ExceptionHandler(FileValidationException::class)
    fun handleFileValidation(ex: FileValidationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "File validation error")))
    }
}
