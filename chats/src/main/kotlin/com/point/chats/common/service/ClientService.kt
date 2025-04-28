package com.point.chats.common.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.chats.v2.chats.rest.response.UserLightweightInfoResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ClientService(private val webClientBuilder: WebClient.Builder) {

    private val webClient = webClientBuilder.baseUrl("http://127.0.0.1:8083/users/api-v2").build()

    fun getUserShortInfo(username: String): UserInfoShortResponse {
        return webClient.get()
            .uri("/{id}", username)
            .header("X-User-Id", username)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(UserInfoShortResponse::class.java)
            .block()!!
    }

    fun getUserLightweightInfo(username: String, ids: List<String>): List<UserLightweightInfoResponse> {
        return webClientBuilder
            .baseUrl("http://127.0.0.1:8083")
            .build()
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/users/api-v2/light")
                    .queryParam("ids", *ids.toTypedArray())
                    .build()
            }
            .header("X-User-Id", username)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<UserLightweightInfoResponse>>() {})
            .block()!!
    }
}

data class UserInfoShortResponse(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("photo")
    val photoId : Long? = null,
)