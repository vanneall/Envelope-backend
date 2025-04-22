package com.point.profiles.contacts.rest.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class UserContactResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("last_photo")
    val lastPhoto: Long?,
)