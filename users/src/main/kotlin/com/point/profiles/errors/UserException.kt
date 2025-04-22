package com.point.profiles.errors

class UserException(val errorCode: ErrorCodes) : RuntimeException(errorCode.name)

