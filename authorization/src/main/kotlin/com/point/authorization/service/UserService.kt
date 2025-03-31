package com.point.authorization.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.point.authorization.data.domain.User
import com.point.authorization.data.domain.toUserInfoRequestBody
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate
import java.util.*

@Service
class UserService(webClientBuilder: WebClient.Builder) {

    private val webClient = webClientBuilder.baseUrl("http://127.0.0.1:8083/users").build()

    fun createUser(user: User, photo: MultipartFile?): ResultInfo {
        val bodyBuilder = MultipartBodyBuilder().apply {
            part("request", user.toUserInfoRequestBody()).contentType(MediaType.APPLICATION_JSON)
            if (photo != null) part("photo", photo).contentType(MediaType.MULTIPART_FORM_DATA)
        }

        return webClient.post()
            .uri("/api-v2")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(bodyBuilder.build())
            .retrieve()
            .bodyToMono(ResultInfo::class.java)
            .block()!!
    }

    fun deleteUser(user: User) =webClient.delete()
            .uri("/api-v2")
            .header("X-User-ID", user.username)
            .retrieve()
            .bodyToMono(ResultInfo::class.java)
            .block()!!
}

data class ResultInfo(
    @JsonProperty("username")
    val username: String,
)

data class UserInfoRequestBody(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("about_user")
    val aboutUser: String? = null,
    @JsonProperty("birth_date")
    val birthDate: LocalDate,
)