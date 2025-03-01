package com.point.profiles.services

import com.point.profiles.data.User
import com.point.profiles.errors.exceptions.PhotoUploadException
import com.point.profiles.errors.exceptions.UserNotFoundException
import com.point.profiles.repository.UserRepository
import com.point.profiles.rest.requests.RegisterRequest
import com.point.profiles.rest.requests.UpdateUserRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.util.*

@Service
class UserService(private val photoService: PhotoService, private val userRepository: UserRepository) {
    fun registerUser(request: RegisterRequest): User {
        val user = User(
            id = request.id,
            name = request.name,
            birthDate = request.birthDate,
        )
        return userRepository.save(user)
    }

    fun getUserById(id: UUID): User {
        return userRepository.findById(id).orElseThrow {
            UserNotFoundException(id.toString())
        }
    }

    fun updateUser(id: UUID, updateRequest: UpdateUserRequest): User {
        val user = getUserById(id)
        if (updateRequest.photo != null) {
            try {
                val id = photoService.uploadPhoto(updateRequest.photo).id
                user.photos.addFirst(id)
            } catch (e: WebClientResponseException) {
                throw PhotoUploadException(status = e.statusCode, message = e.message)
            }
        }

        val updatedUser = user.copy(
            name = updateRequest.name ?: user.name,
            status = updateRequest.status ?: user.status,
            photos = user.photos,
        )
        return userRepository.save(updatedUser)
    }

    fun updateLastSeen(id: UUID) {
        val user = getUserById(id)
        user.lastSeen = Date()
        userRepository.save(user)
    }

    fun addContact(userId: UUID, contactId: UUID) {
        val user = getUserById(userId)
        if (contactId !in user.friends) {
            user.friends.add(contactId)
            userRepository.save(user)
        }
    }

    fun removeContact(userId: UUID, contactId: UUID) {
        val user = getUserById(userId)
        if (contactId in user.friends) {
            user.friends.remove(contactId)
            userRepository.save(user)
        }
    }

    fun blockUser(userId: UUID, blockedId: UUID) {
        val user = getUserById(userId)
        if (blockedId !in user.blockedUsers) {
            user.blockedUsers.add(blockedId)
            userRepository.save(user)
        }
    }

    fun unblockUser(userId: UUID, blockedId: UUID) {
        val user = getUserById(userId)
        if (blockedId in user.blockedUsers) {
            user.blockedUsers.remove(blockedId)
            userRepository.save(user)
        }
    }
}

