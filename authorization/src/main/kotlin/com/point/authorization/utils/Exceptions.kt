package com.point.authorization.utils

class InvalidUserRegistrationCredentials(override val message: String? = "") : RuntimeException()

class InvalidUserAuthorizationCredentials(override val message: String? = "") : RuntimeException()