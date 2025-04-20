package com.point.photo.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteFilesRequest(
    @JsonProperty("ids")
    val ids: List<Long>
)
