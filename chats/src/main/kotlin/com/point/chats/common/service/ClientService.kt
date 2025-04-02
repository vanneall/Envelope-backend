package com.point.chats.common.service

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ClientService(webClientBuilder: WebClient.Builder) {

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
}

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