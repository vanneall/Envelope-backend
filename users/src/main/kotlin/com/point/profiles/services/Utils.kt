package com.point.profiles.services

import com.point.profiles.contacts.rest.responses.UserContactResponse
import com.point.profiles.profile.rest.responses.UserProfileDetailedResponse
import com.point.profiles.requests.rest.response.RequestsInfoResponse
import com.point.profiles.rest.v2.response.OtherUserResponse
import com.point.profiles.rest.v2.response.UserInfoShortResponse
import com.point.profiles.users.data.UserEntity
import org.springframework.transaction.annotation.Transactional


@Transactional
fun UserEntity.toUserShortInfo(userId: String, usernamesWithSentRequest: List<String>): UserInfoShortResponse {
    val inContacts = contacts.any { it.username == userId }
    val inSentRequests = !inContacts && userId in usernamesWithSentRequest
    return UserInfoShortResponse(
        username = username,
        name = name,
        lastSeen = lastSeen,
        status = status,
        about = about,
        birthDate = birthDate,
        photos = photos,
        inContacts = inContacts,
        inSentRequests = inSentRequests,
    )
}

@Transactional
fun UserEntity.toUserDetailedInfo() = UserProfileDetailedResponse(
    username = username,
    name = name,
    lastSeen = lastSeen,
    status = status,
    about = about,
    birthDate = birthDate,
    photos = photos.map { it.toString() },
    friendsCount = contacts.size,
    friends = contacts.map {
        OtherUserResponse(
            username = it.username,
            name = it.name,
            status = it.status,
            lastPhoto = it.photos.firstOrNull(),
            inContacts = true,
            inSentRequests = false,
        )
    },
    blockedCount = blockedUsers.size,
    blockedUsers = blockedUsers.map {
        OtherUserResponse(
            username = it.username,
            name = it.name,
            status = it.status,
            lastPhoto = it.photos.firstOrNull(),
            inContacts = false,
            inSentRequests = false,
        )
    },
)

@Transactional
fun UserEntity.toOtherUserResponse(userId: String, inContacts: Boolean, usernamesWithSentRequest: List<String>): OtherUserResponse {
    val inContacts = inContacts
    val inSentRequests = !inContacts && userId in usernamesWithSentRequest
    return OtherUserResponse(
        username = username,
        name = name,
        status = status,
        lastPhoto = photos.firstOrNull(),
        inContacts = inContacts,
        inSentRequests = inSentRequests,
    )
}

fun UserEntity.toRequestsInfoShort() = RequestsInfoResponse(
    username = username,
    name = name,
    status = status,
    lastPhoto = photos.lastOrNull()
)

fun UserEntity.toUserContactResponse() = UserContactResponse(
    username = username,
    name = name,
    status = status,
    lastPhoto = photos.lastOrNull(),
)