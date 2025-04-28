package com.point.authorization.service.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.point.authorization.data.domain.UserInfo
import com.point.authorization.data.domain.toUserInfoRequestBody
import com.point.authorization.error.exceptions.OtherServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class UserService(webClientBuilder: WebClient.Builder) {

    private val webClient = webClientBuilder.baseUrl("http://127.0.0.1:8083/users/api-v2").build()

    fun createUser(userInfo: UserInfo, photo: MultipartFile?): ResultInfo {
        val bodyBuilder = MultipartBodyBuilder().apply {
            part("request", userInfo.toUserInfoRequestBody()).contentType(MediaType.APPLICATION_JSON)
            if (photo != null) part("photo", photo).contentType(MediaType.MULTIPART_FORM_DATA)
        }

        return webClient.post()
            .uri("/profile")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(bodyBuilder.build())
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java).flatMap { body ->
                    val errorInfo = ObjectMapper().readValue<ErrorInfo>(body)
                    return@flatMap Mono.error(
                        OtherServiceException(
                            HttpStatus.valueOf(response.statusCode().value()),
                            errorInfo.code,
                        )
                    )
                }
            }
            .bodyToMono(ResultInfo::class.java)
            .block()!!
    }
}
