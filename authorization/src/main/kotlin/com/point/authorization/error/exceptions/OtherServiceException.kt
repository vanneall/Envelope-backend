package com.point.authorization.error.exceptions

import com.point.authorization.error.codes.AuthError
import org.springframework.http.HttpStatus

class OtherServiceException(val status: HttpStatus, message: String?) :
    RuntimeException(message ?: AuthError.USER_SERVICE_UNKNOWN_ERROR.name)