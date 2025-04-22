package com.point.profiles.users.rest.controllers

import com.point.profiles.rest.v2.response.UserInfoShortResponse
import com.point.profiles.rest.v2.response.UsersSearchResponse
import com.point.profiles.users.rest.responses.UserLightweightInfoResponse
import com.point.profiles.users.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/api-v2")
class UserController(private val userService: UserService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserShortInfo(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestParam(required = false, defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<UsersSearchResponse> {
        val users = userService.getUsers(username, name, limit, offset)
        return ResponseEntity.status(HttpStatus.OK)
            .headers(headersWithUserId(username))
            .body(users)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserShortInfo(
        @RequestHeader(USER_ID_HEADER) username: String,
        @PathVariable("id") id: String
    ): ResponseEntity<UserInfoShortResponse> {
        val userInfo = userService.getUserInfo(username, id)
        return ResponseEntity.status(HttpStatus.OK)
            .headers(headersWithUserId(username))
            .body(userInfo)
    }

    @GetMapping(
        "/light",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUsernameAndLastPhotoByIds(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestParam(required = false, defaultValue = "") ids: List<String>,
    ): ResponseEntity<List<UserLightweightInfoResponse>> {
        val usersInfo = userService.getUsersLight(ids)
        return ResponseEntity.status(HttpStatus.OK)
            .headers(headersWithUserId(username))
            .body(usersInfo)
    }

    private fun headersWithUserId(userId: String): HttpHeaders = HttpHeaders().apply {
        add(USER_ID_HEADER, userId)
    }

    private companion object {
        const val USER_ID_HEADER = "X-User-ID"
    }
}