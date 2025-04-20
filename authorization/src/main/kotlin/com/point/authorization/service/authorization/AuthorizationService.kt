package com.point.authorization.service.authorization

import com.point.authorization.data.persistence.UserRepository
import com.point.authorization.data.domain.toUserInfo
import com.point.authorization.data.persistence.toEntity
import com.point.authorization.data.request.ForgotPasswordRequest
import com.point.authorization.data.request.UserAuthorizationRequest
import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.data.response.TokenResponse
import com.point.authorization.error.exceptions.NoPasswordMatchesException
import com.point.authorization.error.exceptions.UserAlreadyExists
import com.point.authorization.error.exceptions.UserNotFoundException
import com.point.authorization.service.user.UserService
import com.point.authorization.token.TokenFactory
import com.point.authorization.token.TokenParser
import com.point.authorization.utils.PasswordHasher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class AuthorizationService(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val tokenFactory: TokenFactory,
    private val tokenParser: TokenParser,
) {

    @Transactional(readOnly = true)
    fun login(userAuthorizationRequest: UserAuthorizationRequest): TokenResponse {
        val (username, password) = userAuthorizationRequest
        checkUserValid(username, password)
        return createTokens(username)
    }

    private fun checkUserValid(username: String, password: String) {
        userRepository.findByUsername(username)
            ?.takeIf { it.password == PasswordHasher.hash(password) }
            ?: throw UserNotFoundException()
    }

    @Transactional
    fun register(userRegistration: UserRegistrationRequest, photo: MultipartFile?): TokenResponse {
        if (userRepository.exists(userRegistration.username)) throw UserAlreadyExists()

        val userInfo = userRegistration.toUserInfo()
        userRepository.save(userInfo.toEntity())
        return runCatching { userService.createUser( userInfo = userInfo, photo) }.fold(
            onSuccess = {  createTokens(userInfo.username) },
            onFailure = { throwable ->
                userRepository.deleteById(userInfo.username)
                throw throwable
            },
        )
    }

    @Transactional
    fun resetPassword(forgotPasswordRequest: ForgotPasswordRequest): TokenResponse {
        val user = userRepository.findByUsername(forgotPasswordRequest.username)
        if (user == null) throw UserNotFoundException()

        if (forgotPasswordRequest.newPassword == forgotPasswordRequest.confirmPassword) {
            userRepository.save(user.copy(password = PasswordHasher.hash(forgotPasswordRequest.newPassword)))
        } else {
            throw NoPasswordMatchesException()
        }

        return createTokens(forgotPasswordRequest.username)
    }

    fun refreshAccessToken(refreshToken: String): TokenResponse = createTokens(tokenParser.getUsername(refreshToken))

    private fun createTokens(username: String): TokenResponse {
        val accessToken = tokenFactory.create(username, TokenFactory.Type.ACCESS)
        val refreshToken = tokenFactory.create(username, TokenFactory.Type.REFRESH)
        return TokenResponse(accessToken.value, refreshToken.value)
    }

    fun UserRepository.exists(username: String) = findByUsername(username) != null
}