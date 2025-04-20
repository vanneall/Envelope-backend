package com.point.authorization.error.codes

enum class AuthError {
    // User already exists and can't be created
    USER_EXISTS,
    // When body argument is empty
    ARG_EMPTY,
    // When user not found
    USER_NOT_FOUND,
    // Invalid token at refreshing
    INVALID_TOKEN,
    // When passwords is not matching
    NO_PASSWORD_MATCHES,
    // Unknown error happened on user service
    USER_SERVICE_UNKNOWN_ERROR,
}