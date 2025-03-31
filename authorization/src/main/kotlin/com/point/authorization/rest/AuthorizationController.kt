package com.point.authorization.rest

import com.point.authorization.data.request.UserAuthorizationRequest
import com.point.authorization.service.AuthorizationService
import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.data.response.TokenResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/auth/api-v2")
class AuthorizationController(private val authorizationService: AuthorizationService) {

    @PostMapping("/login")
    fun login(@RequestBody userAuthorizationRequest: UserAuthorizationRequest) =
        TokenResponse(authorizationService.login(userAuthorizationRequest).value)

    @PostMapping(
        "/registration",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun registration(
        @RequestPart("user") userRegistration: UserRegistrationRequest,
        @RequestPart("photo") photo: MultipartFile? = null
    ): ResponseEntity<TokenResponse> {
        val registeredUser = authorizationService.register(userRegistration, photo)
        val token = login(UserAuthorizationRequest(registeredUser.username, registeredUser.password))

        val responseHeaders = HttpHeaders().apply {
            location = URI.create("/users/api-v2/${registeredUser.username}")
            add("Content-Type", "application/json")
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .headers(responseHeaders)
            .body(token)
    }

    @GetMapping("/test")
    fun test() = "OK"
}