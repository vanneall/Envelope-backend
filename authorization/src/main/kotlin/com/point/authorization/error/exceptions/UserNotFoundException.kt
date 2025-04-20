package com.point.authorization.error.exceptions

import com.point.authorization.error.codes.AuthError

class UserNotFoundException : RuntimeException(AuthError.USER_NOT_FOUND.name)