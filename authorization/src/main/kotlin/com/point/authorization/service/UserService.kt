package com.point.authorization.service

import com.point.authorization.data.domain.User
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
class UserService(webClientBuilder: WebClient.Builder) {

    private val webClient = webClientBuilder.baseUrl("http://127.0.0.1:8083/users").build()

    fun createUser(user: User) {
        val bodyBuilder = MultipartBodyBuilder().apply {
            part("id", user.username)
            part("name", user.username)
        }
        webClient.post()
            .uri("")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(bodyBuilder.build())
            .retrieve()
            .bodyToMono(Unit::class.java)
            .block()!!
    }
}