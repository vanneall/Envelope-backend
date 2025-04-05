package com.point.chats.v2.users.rest

import com.point.chats.v2.users.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats/api-v2/users")
class UsersController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestHeader(USER_ID) userId: String): ResponseEntity<String> {
        userService.createUser(userId)
        return ResponseEntity.ok(userId)
    }

    @DeleteMapping
    fun deleteUser(@RequestHeader(USER_ID) userId: String): ResponseEntity<Unit> {
        userService.deleteUser(userId)
        return ResponseEntity.ok().build()
    }

    private companion object {
        const val USER_ID = "X-User-ID"
    }
}