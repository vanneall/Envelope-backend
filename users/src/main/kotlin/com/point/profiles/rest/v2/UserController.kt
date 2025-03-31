package com.point.profiles.rest.v2

import com.point.profiles.rest.v2.request.*
import com.point.profiles.rest.v2.response.OtherUserResponse
import com.point.profiles.rest.v2.response.RegisterUserResponse
import com.point.profiles.rest.v2.response.UserInfoShortResponse
import com.point.profiles.rest.v2.response.UserProfileDetailedResponse
import com.point.profiles.services.UserCommunicationService
import com.point.profiles.services.UserCrudService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/users/api-v2")
class UserController(
    private val userCrudService: UserCrudService,
    private val userCommunicationService: UserCommunicationService,
) {

    @GetMapping("/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserShortInfo(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestParam(required = false, defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<OtherUserResponse>> {
        val usersInfo = userCrudService.getUsersShortInfoByName(userId, name, limit, offset)
        val userContacts = userCommunicationService.getUserFriends(
            username = userId,
            name = name,
            limit = limit,
            offset = offset,
        )

        val concatUsers = (userContacts.toSet() + usersInfo.toSet()).take(limit)

        val responseHeaders = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.status(HttpStatus.OK)
            .headers(responseHeaders)
            .body(concatUsers)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserShortInfo(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @PathVariable("id") id: String
    ): ResponseEntity<UserInfoShortResponse> {
        val userInfo = userCrudService.getUserShortInfo(id, userId)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, id)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.status(HttpStatus.OK)
            .headers(responseHeaders)
            .body(userInfo)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserDetailedInfo(
        @RequestHeader(USER_ID_HEADER) userId: String
    ): ResponseEntity<UserProfileDetailedResponse> {
        val userInfo = userCrudService.getUserDetailedInfo(userId)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.status(HttpStatus.OK)
            .headers(responseHeaders)
            .body(userInfo)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(
        @RequestPart request: UserInfoRequestBody,
        @RequestPart photo: MultipartFile? = null
    ): ResponseEntity<RegisterUserResponse> {
        val username = userCrudService.registerUser(request, photo)

        val responseHeaders = HttpHeaders().apply {
            location = URI.create("/users/api-v2/$username")
            add(USER_ID_HEADER, username)
            add("Content-Type", "application/json")
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .headers(responseHeaders)
            .body(RegisterUserResponse(username))
    }


    @DeleteMapping
    fun deleteUser(
        @RequestHeader(USER_ID_HEADER) userId: String
    ): ResponseEntity<Unit> {
        userCrudService.deleteUser(userId)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .headers(responseHeaders)
            .build()
    }

    @PutMapping
    fun updateUser(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestPart("data") request: UserProfileUpdateRequest,
        @RequestPart("photo", required = false) photo: MultipartFile?,
    ): ResponseEntity<Unit> {
        userCrudService.updateUser(userId, request, photo)
        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .headers(responseHeaders)
            .build()
    }

    @GetMapping("/friends", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFriends(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestParam(required = false, defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<OtherUserResponse>> {
        val friends = userCommunicationService.getUserFriends(
            username = userId,
            name = name,
            limit = limit,
            offset = offset,
        )

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(friends)
    }

    @DeleteMapping("/friends/{username}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteFriend(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @PathVariable username: String,
    ): ResponseEntity<Unit> {
        userCommunicationService.deleteUserFriend(
            userId = userId,
            deletedUserId = username,
        )

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.ok().headers(responseHeaders).body(Unit)
    }

    @GetMapping("/blocked", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBlockedUsers(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestParam(required = false, defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<OtherUserResponse>> {
        val blockedUsers = userCommunicationService.getUserBlocked(
            username = userId,
            name = name,
            limit = limit,
            offset = offset,
        )

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(blockedUsers)
    }

    @PatchMapping("/blocked/block")
    fun blockUser(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestBody request: BlockedUserRequest
    ): ResponseEntity<Unit> {
        userCommunicationService.blockUser(userId, request)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
        }

        return ResponseEntity.noContent()
            .headers(responseHeaders)
            .build()
    }

    @PatchMapping("/blocked/unblock")
    fun unblockUser(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestBody request: BlockedUserRequest
    ): ResponseEntity<Unit> {
        userCommunicationService.unblockUser(userId, request)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
        }

        return ResponseEntity.noContent()
            .headers(responseHeaders)
            .build()
    }

    @GetMapping("/requests", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFriendRequests(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestParam(required = false, defaultValue = "true") incoming: Boolean,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<OtherUserResponse>> {
        val friendRequests = userCommunicationService.getFriendRequests(
            username = userId,
            incoming = incoming,
            limit = limit,
            offset = offset,
        )

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(friendRequests)
    }

    @PatchMapping("/requests")
    fun sendFriendRequest(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestBody request: FriendRequest
    ): ResponseEntity<Unit> {
        userCommunicationService.sendFriendRequest(userId, request)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
        }

        return ResponseEntity.noContent()
            .headers(responseHeaders)
            .build()
    }

    @PatchMapping("/requests/approve")
    fun approveFriendRequest(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestBody request: FriendRequest
    ): ResponseEntity<Unit> {
        userCommunicationService.approveFriendRequest(userId, request)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
        }

        return ResponseEntity.noContent()
            .headers(responseHeaders)
            .build()
    }

    @PatchMapping("/requests/reject")
    fun rejectFriendRequest(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestBody request: FriendRequest
    ): ResponseEntity<Unit> {
        userCommunicationService.rejectFriendRequest(userId, request)

        val responseHeaders = HttpHeaders().apply {
            add(USER_ID_HEADER, userId)
        }

        return ResponseEntity.noContent()
            .headers(responseHeaders)
            .build()
    }

    private companion object {
        const val USER_ID_HEADER = "X-User-ID"
    }
}