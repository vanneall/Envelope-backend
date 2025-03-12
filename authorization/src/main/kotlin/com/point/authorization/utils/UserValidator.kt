package com.point.authorization.utils

import com.point.authorization.data.request.UserRegistrationRequest

object UserValidator {
    fun validate(user: UserRegistrationRequest) {
        if (user.isDeveloper) return

        when {
            !user.username.isValidUserName() -> throw InvalidUserRegistrationCredentials("Username is invalid")
            !user.password.isValidPassword() -> throw  InvalidUserRegistrationCredentials("Password is invalid")
//            !user.telephone.isValidPhoneNumberOrEmpty() -> throw  InvalidUserRegistrationCredentials("Phone number is invalid")
//            !user.email.isValidEmailOrEmpty() -> throw  InvalidUserRegistrationCredentials("Email is invalid")
        }
    }
}

fun String.isValidUserName(): Boolean {
    return this.isNotBlank() && this.length >= 5 && this.length <= 20 && !this.first().isDigit()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() && this.length >= 8
}

fun String?.isValidEmailOrEmpty(): Boolean {
    return this == null || this.isNotBlank() && Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$").matches(this)
}

fun String?.isValidPhoneNumberOrEmpty(): Boolean {
    return this == null || this.isNotBlank() && Regex("^(\\+7|8)[0-9]{10}$").matches(this)
}