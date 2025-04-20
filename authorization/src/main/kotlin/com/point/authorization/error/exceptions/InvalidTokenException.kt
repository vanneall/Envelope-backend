package com.point.authorization.error.exceptions

import com.point.authorization.error.codes.AuthError

class InvalidTokenException : RuntimeException(AuthError.INVALID_TOKEN.name)