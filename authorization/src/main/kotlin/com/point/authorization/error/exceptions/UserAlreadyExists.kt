package com.point.authorization.error.exceptions

import com.point.authorization.error.codes.AuthError

class UserAlreadyExists : RuntimeException(AuthError.USER_EXISTS.name)