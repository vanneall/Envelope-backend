package com.point.profiles.rest


import com.point.profiles.data.User
import com.point.profiles.data.UserInfoShort
import com.point.profiles.rest.requests.RegisterRequest
import com.point.profiles.rest.requests.UpdateUserRequest
import com.point.profiles.rest.requests.UserId
import com.point.profiles.services.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUsers(@RequestParam(value = "name", defaultValue = "") name: String): List<UserInfoShort> {
        return userService.getUsersByName(name)
    }

    @PostMapping
    fun registerUser(@ModelAttribute request: RegisterRequest): User = userService.registerUser(request)

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): User {
        return userService.getUserById(id)
    }

    @PutMapping
    fun updateUser(@RequestHeader("X-User-ID") id: String, @ModelAttribute updateRequest: UpdateUserRequest): User {
        return userService.updateUser(id, updateRequest)
    }

    @PutMapping("/last-seen")
    fun updateLastSeen(@RequestHeader("X-User-ID") id: String) {
        userService.updateLastSeen(id)
    }

    @PostMapping("/contacts")
    fun addContact(@RequestHeader("X-User-ID") id: String, @RequestBody contactId: UserId) {
        userService.addContact(id, contactId.userId)
    }

    @DeleteMapping("/contacts")
    fun removeContact(@RequestHeader("X-User-ID") id: String, @RequestBody contactId: UserId) {
        userService.removeContact(id, contactId.userId)
    }

    @GetMapping("/contacts")
    fun getUserFriendsByName(
        @RequestParam(value = "name", defaultValue = "") name: String,
        @RequestHeader("X-User-ID") userId: String,
    ): List<UserInfoShort> {
        return userService.getUserFriendsByName(userId, name)
    }

    @PostMapping("/blocked")
    fun blockUser(@RequestHeader("X-User-ID") id: String, @RequestBody contactId: UserId) {
        userService.blockUser(id, contactId.userId)
    }

    @DeleteMapping("/blocked")
    fun unblockUser(@PathVariable id: String, @RequestBody contactId: UserId) {
        userService.unblockUser(id, contactId.userId)
    }
}

