package com.point.profiles.rest.v2.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.profiles.rest.v2.request.UserFriendRequest
import java.time.LocalDate
import java.time.LocalDateTime

data class UserProfileDetailedResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("last_seen")
    val lastSeen: LocalDateTime,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("about")
    val about: String?,
    @JsonProperty("birth_date")
    val birthDate: LocalDate,
    @JsonProperty("photos") val
    photos: List<String>,

    @JsonProperty("friends_count")
    val friendsCount: Int,
    @JsonProperty("friends")
    val friends: List<OtherUserResponse>,

    @JsonProperty("blocked_count")
    val blockedCount: Int,
    @JsonProperty("blocked_users")
    val blockedUsers: List<OtherUserResponse>,

    @JsonProperty("friend_requests_count")
    val friendRequestCount: Int,
    @JsonProperty("friend_requests")
    val requests: List<UserFriendRequest>
)
