package com.point.profiles.contacts.rest.controller

import com.point.profiles.contacts.rest.responses.UserContactResponse
import com.point.profiles.contacts.service.ContactsService
import com.point.profiles.rest.v2.response.OtherUserResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/api-v2/contacts")
class ContactsController(private val contactsService: ContactsService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserContacts(
        @RequestHeader(USER_ID_HEADER) username: String,
        @RequestParam(required = false, defaultValue = "") name: String,
        @RequestParam(defaultValue = "35") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<UserContactResponse>> {
        val contacts = contactsService.getContacts(username, name, limit, offset,)

        return ResponseEntity.ok()
            .headers(headersWithUserId(username))
            .body(contacts)
    }

    @DeleteMapping(
        "/{contact}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun deleteUserContact(
        @RequestHeader(USER_ID_HEADER) username: String,
        @PathVariable contact: String,
    ): ResponseEntity<Unit> {
        contactsService.deleteContact(username, contact)

        return ResponseEntity.noContent()
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