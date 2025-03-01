package com.point.photo.exceptions

open class FileValidationException(message: String) : RuntimeException(message)

class InvalidFileExtensionException(message: String) : FileValidationException(message)

class FileSizeExceededException(message: String) : FileValidationException(message)
