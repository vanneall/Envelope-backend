package com.point.profiles.contacts.service

import com.point.profiles.contacts.errors.exceptions.SelfDeleteException
import com.point.profiles.contacts.errors.exceptions.UserNotInContactsException
import com.point.profiles.contacts.repository.ContactsRepository
import com.point.profiles.users.data.UserRepository
import com.point.profiles.services.toUserContactResponse
import com.point.profiles.users.data.findByIdOrThrow
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactsService(private val contactsRepository: ContactsRepository, private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getContacts(username: String, name: String?, limit: Int, offset: Int) =
        contactsRepository.findContactsFiltered(
            username = username,
            query = name ?: "",
            pageable = PageRequest.of(offset, limit),
        ).map { it.toUserContactResponse() }

    @Transactional
    fun deleteContact(username: String, deletedContact: String) {
        if (username == deletedContact) throw SelfDeleteException()

        val user = userRepository.findByIdOrThrow(username)
        val deletedUser = user.contacts.firstOrNull { it.username == deletedContact } ?: throw UserNotInContactsException()

        user.contacts.remove(deletedUser)
        deletedUser.contacts.remove(user)
    }
}