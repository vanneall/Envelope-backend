package com.point.profiles.requests.rest.controller

import com.point.profiles.requests.rest.requests.CreateRequest
import com.point.profiles.requests.rest.requests.HandleRequest
import com.point.profiles.requests.rest.response.RequestResponse
import com.point.profiles.requests.rest.response.RequestsInfoResponse
import com.point.profiles.requests.service.RequestService
import com.point.profiles.rest.v2.response.OtherUserResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/users/api-v2/requests")
class RequestsController(private val requestService: RequestService) {

    @GetMapping(
        "/incoming",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getIncomingRequests(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<RequestsInfoResponse>> {
        val requests = requestService.getIncomingFriendRequests(username, limit, offset)

        return ResponseEntity.ok()
            .headers(headersWithUserId(username))
            .body(requests)
    }

    @GetMapping(
        "/outgoing",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOutgoingRequests(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<RequestsInfoResponse>> {
        val requests = requestService.getOutgoingRequests(username, limit, offset)

        return ResponseEntity.ok()
            .headers(headersWithUserId(username))
            .body(requests)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun createRequest(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestBody createRequest: CreateRequest,
    ): ResponseEntity<RequestResponse> {
        val id = requestService.createRequest(username, createRequest)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/users/api-v2/requests/{id}")
            .buildAndExpand(id)
            .toUri()

        return ResponseEntity.created(location)
            .headers(headersWithUserId(username))
            .body(RequestResponse(id))
    }

    @DeleteMapping(
        "/{requestId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun cancelRequest(
        @RequestHeader(USER_ID_HEADER) username: String,
        @PathVariable requestId: Long
    ): ResponseEntity<Unit> {
        requestService.cancelRequest(username, requestId)

        return ResponseEntity.noContent()
            .headers(headersWithUserId(username))
            .build()
    }

    @PatchMapping(
        "/{requestId}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        )
    fun handleRequest(
        @RequestHeader(USER_ID_HEADER) username: String,
        @PathVariable requestId: Long,
        @RequestBody handleRequest: HandleRequest,
    ): ResponseEntity<Unit> {
        requestService.handleRequestResult(requestId, handleRequest)

        return ResponseEntity.ok()
            .headers(headersWithUserId(username))
            .build()
    }

    private fun headersWithUserId(userId: String): HttpHeaders = HttpHeaders().apply {
        add(USER_ID_HEADER, userId)
    }

    private companion object {
        const val USER_ID_HEADER = "X-User-ID"
    }
}