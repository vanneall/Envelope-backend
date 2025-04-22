package com.point.profiles.profile.errors.exceptions

import org.springframework.http.HttpStatus

class MediaServiceUnknownException(val statusCode: HttpStatus, message: String) : RuntimeException(message)