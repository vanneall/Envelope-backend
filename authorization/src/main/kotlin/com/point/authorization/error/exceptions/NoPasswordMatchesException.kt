package com.point.authorization.error.exceptions

import com.point.authorization.error.codes.AuthError

class NoPasswordMatchesException : RuntimeException(AuthError.NO_PASSWORD_MATCHES.name)