package com.point.profiles.profile.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UserProfileUpdateRequest(
    @JsonProperty("name")
    val name: String? = null,
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("about")
    val about: String? = null,
    @JsonProperty("birth_date")
    val birthDate: LocalDate? = null,
)