package com.point.photo.rest.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class ExceptionResponse(@JsonProperty("code") val code: String)