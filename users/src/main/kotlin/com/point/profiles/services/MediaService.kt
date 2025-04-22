package com.point.profiles.services

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.point.profiles.profile.errors.exceptions.MediaServiceUnknownException
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class MediaService(webClientBuilder: WebClient.Builder) {

    private val webClient = webClientBuilder.baseUrl("http://127.0.0.1:8084/media").build()

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

    fun deletePhoto(photoId: Long) = webClient.delete()
        .uri("/$photoId")
        .retrieve()
        .onStatus({ it.isError }) { response ->
            response.bodyToMono(String::class.java).flatMap { body ->
                val errorInfo = ObjectMapper().readValue<ErrorInfo>(body)
                return@flatMap Mono.error(
                    MediaServiceUnknownException(
                        HttpStatus.valueOf(response.statusCode().value()),
                        errorInfo.code,
                    )
                )
            }
        }
        .bodyToMono<Unit>()
        .block()!!

    fun deletePhotos(photoIds: List<Long>) {
        if (photoIds.isEmpty()) return

        webClient.method(HttpMethod.DELETE)
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(photoIds)
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java).flatMap { body ->
                    val errorInfo = ObjectMapper().readValue<ErrorInfo>(body)
                    Mono.error(
                        MediaServiceUnknownException(
                            HttpStatus.valueOf(response.statusCode().value()),
                            errorInfo.code
                        )
                    )
                }
            }
            .bodyToMono<Unit>()
            .block()!!
    }
}

data class PhotoIdResponse(@JsonProperty("id") val id: Long)