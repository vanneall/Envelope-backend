package com.point.profiles.profile.service

import com.point.profiles.profile.errors.exceptions.SaveProfileException
import com.point.profiles.profile.errors.exceptions.UserAlreadyExistsException
import com.point.profiles.users.data.UserRepository
import com.point.profiles.profile.rest.requests.UserInfoRequestBody
import com.point.profiles.profile.rest.requests.UserProfileUpdateRequest
import com.point.profiles.services.MediaService
import com.point.profiles.services.toUserDetailedInfo
import com.point.profiles.users.data.UserEntity
import com.point.profiles.users.data.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class ProfileService(private val userRepository: UserRepository, private val mediaService: MediaService) {

    @Transactional(readOnly = true)
    fun getUserDetailedInfo(username: String) = userRepository.findByIdOrThrow(username).toUserDetailedInfo()

    @Transactional
    fun createUser(data: UserInfoRequestBody, photo: MultipartFile? = null): String {
        if (userRepository.existsById(data.username)) throw UserAlreadyExistsException()

        val photoId = photo?.let { mediaService.uploadPhoto(photo).id }

        return runCatching { userRepository.save(
            UserEntity(
                username = data.username,
                name = data.name,
                status = data.status,
                about = data.aboutUser,
                birthDate = data.birthDate,
                lastSeen = LocalDateTime.now(),
                photos = listOfNotNull(photoId).toMutableList()
            )
        ) }.fold(
            onSuccess = { it.username },
            onFailure = {
                photoId?.let { mediaService.deletePhoto(it) }
                throw SaveProfileException()
            }
        )
    }

    @Transactional
    fun deleteUser(username: String) {
        val deletedUser = userRepository.findByIdOrThrow(username)
        userRepository.delete(deletedUser)
        mediaService.deletePhotos(deletedUser.photos)
    }

    @Transactional
    fun updateUser(username: String, data: UserProfileUpdateRequest, photo: MultipartFile? = null) {
        val user = userRepository.findByIdOrThrow(username)

        val photoId = photo?.let { mediaService.uploadPhoto(photo).id }

        userRepository.save(
            user.copy(
                name = data.name ?: user.name,
                status = data.status ?: user.status,
                about = data.about ?: user.about,
                birthDate = data.birthDate ?: user.birthDate,
            )
        )
        photoId?.let { user.photos.addFirst(photoId) }
    }
}