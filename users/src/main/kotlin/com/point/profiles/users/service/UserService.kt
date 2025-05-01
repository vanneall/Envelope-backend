package com.point.profiles.users.service

import com.point.profiles.requests.data.RequestRepository
import com.point.profiles.rest.v2.response.UserInfoShortResponse
import com.point.profiles.users.data.UserRepository
import com.point.profiles.rest.v2.response.UsersSearchResponse
import com.point.profiles.users.rest.responses.toResponse
import com.point.profiles.users.data.UserLightweightInfo
import com.point.profiles.services.toOtherUserResponse
import com.point.profiles.services.toUserShortInfo
import com.point.profiles.users.data.findByIdOrThrow
import org.hibernate.Hibernate
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository, private val requestRepository: RequestRepository) {

    @Transactional(readOnly = true)
    fun getUsers(username: String, name: String, limit: Int, offset: Int): UsersSearchResponse {
        val userContacts = userRepository.findContactsOfUser(username)
        val globalUsers = userRepository
            .findByNameLikeIgnoreCase(name, PageRequest.of(offset, limit))
            .toList()

        val requests = requestRepository.findOutgoingRequests(username, PageRequest.of(0, 100))
            .toList()
            .map { it.consumer.username }

        val inContacts = globalUsers.filter { globalUser -> globalUser in userContacts }
        val notInContacts = globalUsers.filterNot { globalUser -> globalUser in userContacts }

        return UsersSearchResponse(
            inContacts = inContacts.map { it.toOtherUserResponse(username, true, requests) },
            allContacts = notInContacts.map { it.toOtherUserResponse(username, false, requests) },
        )
    }

    @Transactional(readOnly = true)
    fun getUsersLight(usernames: List<String>) = userRepository.findAllById(usernames).map { entity ->
        UserLightweightInfo(
            username = entity.username,
            name = entity.name,
            photoId = entity.photos.firstOrNull(),
        )
    }.map { info -> info.toResponse() }.toList()

    @Transactional(readOnly = true)
    fun getUserInfo(username: String, id: String): UserInfoShortResponse {
        val requests = requestRepository.findOutgoingRequests(username, PageRequest.of(0, 100))
            .toList()
            .map { it.consumer.username }
        println("точно")
        val user = userRepository.findByIdOrThrow(id)
        println("разъебалось")
        return user.toUserShortInfo(username, username in requestRepository.findContactUsernamesByOwner(id), requests)
    }
}