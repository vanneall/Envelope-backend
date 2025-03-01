package com.point.profiles.errors.exceptions

import org.springframework.http.HttpStatusCode

class PhotoUploadException(val status: HttpStatusCode, message: String) : RuntimeException("Something went wrong when uploading photo: $message")