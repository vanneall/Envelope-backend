package com.point.profiles.exceptions

class UserException(val errorCode: ErrorCodes) : RuntimeException(errorCode.name)

