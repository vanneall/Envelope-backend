package com.point.authorization.rest.controller

import com.point.authorization.data.domain.EmailRequest
import com.point.authorization.data.request.ForgotPasswordRequest
import com.point.authorization.data.request.RefreshTokenRequest
import com.point.authorization.data.request.UserAuthorizationRequest
import com.point.authorization.data.request.UserRegistrationRequest
import com.point.authorization.data.response.TokenResponse
import com.point.authorization.service.authorization.AuthorizationService
import com.point.authorization.service.authorization.EmailCodeService
import jakarta.validation.Valid
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/auth/api-v2")
class AuthorizationController(
    private val authorizationService: AuthorizationService,
    private val email: EmailCodeService,
) {

    @PostMapping(
        "/login",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(@RequestBody @Valid userAuthorizationRequest: UserAuthorizationRequest): ResponseEntity<TokenResponse> {
        val tokens = authorizationService.login(userAuthorizationRequest)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noStore())
            .body(tokens)
    }


    @PostMapping(
        "/registration",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun postRegistration(
        @RequestPart("user") @Valid userRegistration: UserRegistrationRequest,
        @RequestPart("photo") photo: MultipartFile? = null
    ): ResponseEntity<TokenResponse> {
        val tokens = authorizationService.register(userRegistration, photo)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/users/api-v2/{username}")
            .buildAndExpand(userRegistration.username)
            .toUri()

        return ResponseEntity.created(location)
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noStore())
            .body(tokens)
    }

    @PostMapping(
        "/refresh",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun postRefreshToken(
        @RequestBody @Valid refreshTokenRequest: RefreshTokenRequest,
    ): ResponseEntity<TokenResponse> {
        val tokens = authorizationService.refreshAccessToken(refreshTokenRequest.refreshToken)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(tokens)
    }

    @PatchMapping(
        "/reset",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun postResetPassword(
        @RequestBody @Valid forgotPasswordRequest: ForgotPasswordRequest,
    ): ResponseEntity<TokenResponse> {
        val tokens = authorizationService.resetPassword(forgotPasswordRequest)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(tokens)
    }

    @PostMapping("/send-code")
    fun sendEmailCode(@RequestBody request: EmailRequest): ResponseEntity<Unit> {
        email.sendCode(request.email)
        return ResponseEntity.ok().build()
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/test")
    fun test() = Unit
}