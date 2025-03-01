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
        val photos = if (request.photo != null) {
            try {
                val id = photoService.uploadPhoto(request.photo).id
                mutableListOf(id)
            } catch (e: WebClientResponseException) {
                throw PhotoUploadException(status = e.statusCode, message = e.message)
            }
        } else {
            mutableListOf()
        }

        val user = User(
            id = request.id,
            name = request.name,
            birthDate = request.birthDate,
            photos = photos,
        )
        return userRepository.save(user)
    }

    fun getUserById(id: String): User {
        return userRepository.findById(id).orElseThrow {
            UserNotFoundException(id)
        }
    }

    fun updateUser(id: String, updateRequest: UpdateUserRequest): User {
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

    fun updateLastSeen(id: String) {
        val user = getUserById(id)
        user.lastSeen = Date()
        userRepository.save(user)
    }

    fun addContact(userId: String, contactId: String) {
        val user = getUserById(userId)
        if (contactId !in user.friends) {
            user.friends.add(contactId)
            userRepository.save(user)
        }
    }

    fun removeContact(userId: String, contactId: String) {
        val user = getUserById(userId)
        if (contactId in user.friends) {
            user.friends.remove(contactId)
            userRepository.save(user)
        }
    }

    fun blockUser(userId: String, blockedId: String) {
        val user = getUserById(userId)
        if (blockedId !in user.blockedUsers) {
            user.blockedUsers.add(blockedId)
            userRepository.save(user)
        }
    }

    fun unblockUser(userId: String, blockedId: String) {
        val user = getUserById(userId)
        if (blockedId in user.blockedUsers) {
            user.blockedUsers.remove(blockedId)
            userRepository.save(user)
        }
    }
}

