package com.point.profiles.services

import com.point.profiles.exceptions.ErrorCodes
import com.point.profiles.exceptions.UserException
import com.point.profiles.repository.UserEntity
import com.point.profiles.repository.UserRepository
import com.point.profiles.rest.v2.request.UserInfoRequestBody
import com.point.profiles.rest.v2.request.UserProfileUpdateRequest
import com.point.profiles.rest.v2.response.OtherUserResponse
import com.point.profiles.rest.v2.response.UserInfoShortResponse
import com.point.profiles.rest.v2.response.UserProfileDetailedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserCrudService(private val userRepository: UserRepository, private val photoService: PhotoService) {

    private fun findUserByUsername(username: String): UserEntity =
        userRepository.findById(username).orElseThrow { UserException(ErrorCodes.USER_NOT_FOUND) }

    fun getUserShortInfo(username: String, userId: String): UserInfoShortResponse =
        findUserByUsername(username).toUserShortInfo(userId)

    fun getUsersShortInfoByName(userId: String, name: String, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findByNameContainingIgnoreCase(name, PageRequest.of(offset, limit))
            .content
            .filter { it.username != userId }
            .map { user ->
                user.toOtherUserResponse(userId)
            }

    fun getUserDetailedInfo(username: String): UserProfileDetailedResponse =
        findUserByUsername(username).toUserDetailedInfo()

    fun registerUser(request: UserInfoRequestBody, photo: MultipartFile?): String {
        if (request.username.isBlank()) {
            throw UserException(ErrorCodes.USERNAME_EMPTY)
        }
        if (request.name.isBlank()) {
            throw UserException(ErrorCodes.NAME_EMPTY)
        }
        if (request.birthDate.isAfter(LocalDate.now())) {
            throw UserException(ErrorCodes.BIRTHDATE_INVALID)
        }

        if (userRepository.existsById(request.username)) {
            throw UserException(ErrorCodes.USER_ALREADY_EXISTS)
        }

        val photoId = try {
            photo?.let { photoService.uploadPhoto(it).id }
        } catch (e: Exception) {
            throw UserException(ErrorCodes.PHOTO_UPLOAD_FAILED)
        }

        val newUser = UserEntity(
            username = request.username,
            name = request.name,
            status = request.status,
            about = request.aboutUser,
            birthDate = request.birthDate,
            lastSeen = LocalDateTime.now(),
            photos = listOfNotNull(photoId)
        )

        userRepository.save(newUser)

        return newUser.username
    }


    fun deleteUser(username: String) {
        val user = findUserByUsername(username)
        userRepository.delete(user)
    }

    fun updateUser(username: String, request: UserProfileUpdateRequest, photo: MultipartFile?) {
        val user = findUserByUsername(username)

        if (request.name != null && request.name.isBlank()) {
            throw UserException(ErrorCodes.NAME_EMPTY)
        }
        if (request.birthDate != null && request.birthDate.isAfter(LocalDate.now())) {
            throw UserException(ErrorCodes.BIRTHDATE_INVALID)
        }

        val photoId = try {
            photo?.let { photoService.uploadPhoto(it).id }
        } catch (e: Exception) {
            throw UserException(ErrorCodes.PHOTO_UPLOAD_FAILED)
        }

        val updatedUser = user.copy(
            name = request.name ?: user.name,
            status = request.status ?: user.status,
            about = request.about ?: user.about,
            birthDate = request.birthDate ?: user.birthDate,
            photos = photoId?.let { listOf(it) + user.photos } ?: user.photos
        )

        userRepository.save(updatedUser)
    }

    @Transactional
    fun getUserLightweightInfo(users: List<String>) = userRepository.findAllById(users).map { entity ->
        UserLightweightInfo(
            username = entity.username,
            name = entity.name,
            photoId = entity.photos.lastOrNull(),
        )
    }
}

data class UserLightweightInfo(
    val username: String,
    val name: String,
    val photoId : Long? = null,
)