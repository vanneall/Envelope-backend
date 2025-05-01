package com.point.profiles.contacts.service

import com.point.profiles.contacts.errors.exceptions.SelfDeleteException
import com.point.profiles.contacts.errors.exceptions.UserNotInContactsException
import com.point.profiles.contacts.repository.ContactsRepository
import com.point.profiles.users.data.UserRepository
import com.point.profiles.services.toUserContactResponse
import com.point.profiles.users.data.ContactRepository
import com.point.profiles.users.data.findByIdOrThrow
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactsService(
    private val contactsRepository: ContactsRepository,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
) {

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
        val deletedContact =
            user.contacts.firstOrNull { it.contact.username == deletedContact } ?: throw UserNotInContactsException()

        deletedContact.owner.contacts.remove(deletedContact)
        deletedContact.contact.contacts.remove(deletedContact)
        contactRepository.delete(deletedContact)
    }
}