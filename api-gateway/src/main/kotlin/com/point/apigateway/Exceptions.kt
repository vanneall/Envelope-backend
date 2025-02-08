package com.point.apigateway

sealed class SecurityException(override val message: String? = "") : RuntimeException() {

    data object TokenExpiredException : SecurityException("Token expired") {
        private fun readResolve(): Any = TokenExpiredException
    }

    data object InvalidTokenException : SecurityException("Invalid token") {
        private fun readResolve(): Any = InvalidTokenException
    }
}