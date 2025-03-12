package com.point.profiles.services

import com.point.profiles.repository.UserEntity
import com.point.profiles.rest.v2.request.UserFriendRequest
import com.point.profiles.rest.v2.response.OtherUserResponse
import com.point.profiles.rest.v2.response.UserInfoShortResponse
import com.point.profiles.rest.v2.response.UserProfileDetailedResponse
import org.springframework.transaction.annotation.Transactional


@Transactional
fun UserEntity.toUserShortInfo() = UserInfoShortResponse(
    username = username,
    name = name,
    lastSeen = lastSeen,
    status = status,
    about = about,
    birthDate = birthDate,
    photos = photos
)

@Transactional
fun UserEntity.toUserDetailedInfo() = UserProfileDetailedResponse(
    username = username,
    name = name,
    lastSeen = lastSeen,
    status = status,
    about = about,
    birthDate = birthDate,
    photos = photos.map { it.toString() },
    friendsCount = friends.size,
    friends = friends.map {
        OtherUserResponse(
            username = it.username,
            name = it.name,
            status = it.status,
            lastPhoto = it.photos.firstOrNull(),
        )
    },
    blockedCount = blockedUsers.size,
    blockedUsers = blockedUsers.map {
        OtherUserResponse(
            username = it.username,
            name = it.name,
            status = it.status,
            lastPhoto = it.photos.firstOrNull(),
        )
    },
    friendRequestCount = receivedFriendRequests.size,
    requests = receivedFriendRequests.map {
        UserFriendRequest(
            id = requireNotNull(it.id),
            userId = it.sender.username
        )
    }
)

@Transactional
fun UserEntity.toOtherUserResponse() = OtherUserResponse(
    username = username,
    name = name,
    status = status,
    lastPhoto = photos.firstOrNull(),
)