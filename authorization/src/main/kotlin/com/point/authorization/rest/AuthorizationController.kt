package com.point.authorization.rest

import com.point.authorization.data.persistence.toEntity
import com.point.authorization.data.request.UserAuthorizationRequest
import com.point.authorization.service.AuthorizationService
import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.data.response.TokenResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthorizationController(private val authorizationService: AuthorizationService) {

    @PostMapping("/api-v1/login")
    fun login(@RequestBody userAuthorizationRequest: UserAuthorizationRequest) =
        TokenResponse(authorizationService.login(userAuthorizationRequest).value)

    @PostMapping("/api-v1/registration")
    fun registration(@RequestBody user: UserRegistrationRequest): TokenResponse {
        val registeredUser = authorizationService.register(user)
        return login(UserAuthorizationRequest(registeredUser.username, registeredUser.password))
    }

    @GetMapping("/api-v1/test")
    fun test() = "OK"
}