package com.point.profiles.errors

import com.fasterxml.jackson.annotation.JsonProperty

data class ExceptionResponse(@JsonProperty("code") val code: String)