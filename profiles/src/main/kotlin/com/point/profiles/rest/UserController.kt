package com.point.profiles.rest


import com.point.profiles.data.User
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

    @PostMapping
    fun registerUser(@RequestBody request: RegisterRequest): User = userService.registerUser(request)

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): User {
        return userService.getUserById(id)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody updateRequest: UpdateUserRequest): User {
        return userService.updateUser(id, updateRequest)
    }

    @PutMapping("/{id}/last-seen")
    fun updateLastSeen(@PathVariable id: UUID) {
        userService.updateLastSeen(id)
    }

    @PostMapping("/{id}/contacts/{contactId}")
    fun addContact(@PathVariable id: UUID, @PathVariable contactId: UUID) {
        userService.addContact(id, contactId)
    }

    @DeleteMapping("/{id}/contacts/{contactId}")
    fun removeContact(@PathVariable id: UUID, @PathVariable contactId: UUID) {
        userService.removeContact(id, contactId)
    }

    @PostMapping("/{id}/block/{blockedId}")
    fun blockUser(@PathVariable id: UUID, @PathVariable blockedId: UUID) {
        userService.blockUser(id, blockedId)
    }

    @DeleteMapping("/{id}/block/{blockedId}")
    fun unblockUser(@PathVariable id: UUID, @PathVariable blockedId: UUID) {
        userService.unblockUser(id, blockedId)
    }
}

