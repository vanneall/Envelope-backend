package com.point.profiles.rest


import com.point.profiles.data.User
import com.point.profiles.data.UserInfoShort
import com.point.profiles.rest.requests.RegisterRequest
import com.point.profiles.rest.requests.UpdateUserRequest
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

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @ModelAttribute updateRequest: UpdateUserRequest): User {
        return userService.updateUser(id, updateRequest)
    }

    @PutMapping("/{id}/last-seen")
    fun updateLastSeen(@PathVariable id: String) {
        userService.updateLastSeen(id)
    }

    @PostMapping("/{id}/contacts/{contactId}")
    fun addContact(@PathVariable id: String, @PathVariable contactId: String) {
        userService.addContact(id, contactId)
    }

    @DeleteMapping("/{id}/contacts/{contactId}")
    fun removeContact(@PathVariable id: String, @PathVariable contactId: String) {
        userService.removeContact(id, contactId)
    }

    @PostMapping("/{id}/block/{blockedId}")
    fun blockUser(@PathVariable id: String, @PathVariable blockedId: String) {
        userService.blockUser(id, blockedId)
    }

    @DeleteMapping("/{id}/block/{blockedId}")
    fun unblockUser(@PathVariable id: String, @PathVariable blockedId: String) {
        userService.unblockUser(id, blockedId)
    }
}

