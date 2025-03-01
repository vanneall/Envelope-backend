package com.point.chats.common.service

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Service
class PhotoService(webClientBuilder: WebClient.Builder) {

    private val webClient = webClientBuilder.baseUrl("http://127.0.0.1:8084/photos").build()

    fun uploadPhoto(photo: MultipartFile): PhotoIdResponse {
        val bodyBuilder = MultipartBodyBuilder()
        bodyBuilder.part("photo", photo.resource)
        return webClient.post()
            .uri("")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
            .retrieve()
            .bodyToMono(PhotoIdResponse::class.java)
            .block()!!
    }
}

data class PhotoIdResponse(@JsonProperty("id") val id: Long)