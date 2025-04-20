package com.point.photo.rest.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class FileCreatedResponse(
    @JsonProperty(value = "id")
    val id: Long,
)