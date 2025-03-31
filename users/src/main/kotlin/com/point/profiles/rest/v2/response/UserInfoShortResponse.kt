package com.point.profiles.rest.v2.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime

data class UserInfoShortResponse(
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
    @JsonProperty("photos")
    val photos: List<Long>,
    @JsonProperty("in_contacts")
    val inContacts: Boolean,
    @JsonProperty("in_sent_requests")
    val inSentRequests: Boolean,
)