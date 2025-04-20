package com.point.photo.rest.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class FilesCreatedResponse(
    @JsonProperty(value = "ids")
    val ids: List<Long>,
)