package com.point.profiles.requests.service

import com.point.profiles.errors.ErrorCodes
import com.point.profiles.requests.errors.exceptions.RequestNotFoundException
import com.point.profiles.errors.UserException
import com.point.profiles.requests.data.RequestEntity
import com.point.profiles.requests.data.RequestRepository
import com.point.profiles.users.data.UserRepository
import com.point.profiles.requests.rest.requests.CreateRequest
import com.point.profiles.requests.rest.requests.HandleRequest
import com.point.profiles.requests.rest.requests.RequestHandleType
import com.point.profiles.services.toRequestsInfoShort
import com.point.profiles.users.data.ContactEntity
import com.point.profiles.users.data.ContactRepository
import com.point.profiles.users.data.findByIdOrThrow
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RequestService(
    private val requestRepository: RequestRepository,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
) {

    @Transactional(readOnly = true)
    fun getIncomingFriendRequests(username: String, limit: Int, offset: Int) =
        requestRepository.findIncomingRequests(username, PageRequest.of(offset, limit))
            .toList()
            .map { request -> request.producer.toRequestsInfoShort(requireNotNull(request.id)) }

    @Transactional(readOnly = true)
    fun getOutgoingRequests(username: String, limit: Int, offset: Int) =
        requestRepository.findOutgoingRequests(username, PageRequest.of(offset, limit))
            .toList()
            .map { request -> request.consumer.toRequestsInfoShort(requireNotNull(request.id)) }

    @Transactional
    fun createRequest(producerId: String, createRequest: CreateRequest): Long {
        val producer = userRepository.findByIdOrThrow(producerId)
        val consumer = userRepository.findByIdOrThrow(createRequest.userId)

        if (producer.username == consumer.username) {
            throw UserException(ErrorCodes.CANNOT_ADD_SELF)
        }

        val req = requestRepository.findContactUsernamesByOwner(producer.username)
        if (consumer.username in req) throw UserException(ErrorCodes.USER_ALREADY_FRIEND)

        if (requestExistsBetween(producer.username, consumer.username))
            throw UserException(ErrorCodes.REQUEST_ALREADY_SENT)

        val request = RequestEntity(producer = producer, consumer = consumer)
        return requestRepository.save(request).id!!
    }

    fun requestExistsBetween(userA: String, userB: String): Boolean {
        return requestRepository.existsByProducer_UsernameAndConsumer_Username(userA, userB) ||
                requestRepository.existsByProducer_UsernameAndConsumer_Username(userB, userA)
    }

    @Transactional
    fun cancelRequest(username: String, requestId: Long) {
        val request = requestRepository.findByIdOrThrow(requestId)

        if (request.producer.username != username) throw UserException(ErrorCodes.REQUEST_NOT_OWNED)

        requestRepository.delete(request)
    }

    @Transactional
    fun handleRequestResult(requestId: Long, request: HandleRequest) = when (request.result) {
        RequestHandleType.ACCEPT -> acceptRequest(requestId)
        RequestHandleType.REJECT -> rejectRequest(requestId)
    }

    private fun acceptRequest(requestId: Long) {
        val request = requestRepository.findByIdOrThrow(requestId)

        val producer = request.producer
        val consumer = request.consumer

        val contact1 = ContactEntity(owner = producer, contact = consumer)
        val contact2 = ContactEntity(owner = consumer, contact = producer)

        contactRepository.save(contact1)
        contactRepository.save(contact2)

        requestRepository.delete(request)
    }

    private fun rejectRequest(requestId: Long) {
        requestRepository.delete(requestRepository.findByIdOrThrow(requestId))
    }
}

fun RequestRepository.findByIdOrThrow(id: Long): RequestEntity =
    findById(id).orElseThrow { RequestNotFoundException() }