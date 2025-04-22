package com.point.profiles.profile.rest.controllers

import com.point.profiles.profile.service.ProfileService
import com.point.profiles.profile.rest.requests.UserInfoRequestBody
import com.point.profiles.profile.rest.requests.UserProfileUpdateRequest
import com.point.profiles.profile.rest.responses.RegisterUserResponse
import com.point.profiles.profile.rest.responses.UserProfileDetailedResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/users/api-v2/profile")
class ProfileController(private val profileService: ProfileService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserDetailedInfo(
        @RequestHeader(USER_ID_HEADER) username: String,
    ): ResponseEntity<UserProfileDetailedResponse> {
        val userInfo = profileService.getUserDetailedInfo(username)
        return ResponseEntity.status(HttpStatus.OK)
            .headers(headersWithUserId(username))
            .body(userInfo)
    }

    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun createUser(
        @RequestPart @Valid request: UserInfoRequestBody,
        @RequestPart photo: MultipartFile? = null
    ): ResponseEntity<RegisterUserResponse> {
        val username = profileService.createUser(request, photo)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/users/api-v2/{username}")
            .buildAndExpand(username)
            .toUri()

        return ResponseEntity.created(location)
            .headers(headersWithUserId(username))
            .body(RegisterUserResponse(username))
    }

    @DeleteMapping
    fun deleteUser(@RequestHeader(USER_ID_HEADER) username: String): ResponseEntity<Unit> {
        profileService.deleteUser(username)
        return ResponseEntity.noContent()
            .headers(headersWithUserId(username))
            .build()
    }

    @PatchMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun updateUser(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestPart("data") request: UserProfileUpdateRequest,
        @RequestPart("photo", required = false) photo: MultipartFile?,
    ): ResponseEntity<Unit> {
        profileService.updateUser(username, request, photo)
        return ResponseEntity.ok()
            .headers(headersWithUserId(username))
            .build()
    }

    private fun headersWithUserId(userId: String): HttpHeaders = HttpHeaders().apply {
        add(USER_ID_HEADER, userId)
    }

    private companion object {
        const val USER_ID_HEADER = "X-User-ID"
    }
}