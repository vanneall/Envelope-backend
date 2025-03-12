package com.point.authorization.service

import com.point.authorization.UserRepository
import com.point.authorization.data.domain.User
import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.data.domain.toUser
import com.point.authorization.data.persistence.toEntity
import com.point.authorization.data.request.UserAuthorizationRequest
import com.point.authorization.token.Token
import com.point.authorization.token.TokenFactory
import com.point.authorization.utils.InvalidUserRegistrationCredentials
import com.point.authorization.utils.PasswordHasher
import com.point.authorization.utils.UserValidator
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AuthorizationService(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val tokenFactory: TokenFactory
) {

    fun login(userAuthorizationRequest: UserAuthorizationRequest): Token {
        val (username, password) = userAuthorizationRequest
        val userByUsername = userRepository.findByUsername(username)
            ?: throw InvalidUserRegistrationCredentials("Invalid login or password")
        if (userByUsername.password != PasswordHasher.hash(password)) throw InvalidUserRegistrationCredentials("Invalid login or password")

        return tokenFactory.create(username)
    }

    fun register(userRegistration: UserRegistrationRequest, photo: MultipartFile?): User {
        UserValidator.validate(userRegistration)
        val user = userRegistration.toUser()
        try {
            userService.createUser(user, photo)
        } catch (ex: Exception) {
            throw InvalidUserRegistrationCredentials(ex.message)
        }

        try {
            userRepository.save(user.toEntity())
        } catch (ex: Exception) {
            userService.deleteUser(user)
            throw InvalidUserRegistrationCredentials(ex.message)
        }

        return user
    }
}