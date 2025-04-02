package com.point.profiles.rest.v2.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UsersSearchResponse(
    @JsonProperty("in_contacts")
    val inContacts: List<OtherUserResponse>,
    @JsonProperty("all_contacts")
    val allContacts: List<OtherUserResponse>,
)

data class OtherUserResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("last_photo")
    val lastPhoto: Long?,
    @JsonProperty("in_contacts")
    val inContacts: Boolean,
    @JsonProperty("in_sent_requests")
    val inSentRequests: Boolean,
)