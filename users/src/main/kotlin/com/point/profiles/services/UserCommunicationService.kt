package com.point.profiles.services

import com.point.profiles.exceptions.ErrorCodes
import com.point.profiles.exceptions.UserException
import com.point.profiles.repository.UserEntity
import com.point.profiles.repository.UserRepository
import com.point.profiles.rest.v2.request.*
import com.point.profiles.rest.v2.response.OtherUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserCommunicationService(private val userRepository: UserRepository) {

    private fun findUserByUsername(username: String): UserEntity =
        userRepository.findById(username).orElseThrow { UserException(ErrorCodes.USER_NOT_FOUND) }

    @Transactional
    fun getUserFriends(username: String, name: String?, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findFriendsWithPagination(
            username = username,
            name = name,
            limit = limit,
            offset = offset,
        ).map { it.toOtherUserResponse() }

    @Transactional
    fun getUserBlocked(username: String, name: String?, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findBlockedUsersWithPagination(
            username = username,
            name = name,
            limit = limit,
            offset = offset,
        ).map { it.toOtherUserResponse() }

    @Transactional
    fun getFriendRequests(username: String, name: String?, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findFriendRequestsWithPagination(username, limit, offset)
            .map { it.toOtherUserResponse() }

    @Transactional
    fun blockUser(userId: String, request: BlockedUserRequest) {
        val user = findUserByUsername(userId)
        val blockedUser = findUserByUsername(request.blockedUserId)

        if (blockedUser in user.blockedUsers) {
            return
        }

        if (userId == request.blockedUserId) {
            throw UserException(ErrorCodes.INVALID_BLOCK_MYSELF)
        }

        val updatedUser = user.copy(blockedUsers = user.blockedUsers + blockedUser)
        userRepository.save(updatedUser)
    }

    @Transactional
    fun unblockUser(userId: String, request: BlockedUserRequest) {
        val user = findUserByUsername(userId)
        val blockedUser = findUserByUsername(request.blockedUserId)

        if (blockedUser !in user.blockedUsers) {
            return
        }

        val updatedUser = user.copy(blockedUsers = user.blockedUsers - blockedUser)
        userRepository.save(updatedUser)
    }

    @Transactional
    fun sendFriendRequest(senderId: String, request: FriendRequest) {
        val sender = findUserByUsername(senderId)
        val receiver = findUserByUsername(request.otherId)

        if (receiver in sender.friends) {
            throw UserException(ErrorCodes.USER_ALREADY_FRIEND)
        }

        if (receiver in sender.blockedUsers || sender in receiver.blockedUsers) {
            throw UserException(ErrorCodes.USER_BLOCKED)
        }

        if (sender.sentFriendRequests.any { it.receiverId == receiver.username }) {
            throw UserException(ErrorCodes.REQUEST_ALREADY_SENT)
        }

        val newRequest = UserEntity.FriendRequestEntity(
            senderId = sender.username,
            receiverId = receiver.username
        )

        userRepository.save(sender.copy(sentFriendRequests = sender.sentFriendRequests + newRequest))
        userRepository.save(receiver.copy(receivedFriendRequests = receiver.receivedFriendRequests + newRequest))
    }

    @Transactional
    fun approveFriendRequest(receiverId: String, request: FriendRequest) {
        val receiver = findUserByUsername(receiverId)
        val sender = findUserByUsername(request.otherId)

        if (sender in receiver.friends) {
            throw UserException(ErrorCodes.USER_ALREADY_FRIEND)
        }

        val updatedReceiver = receiver.copy(friends = receiver.friends + sender)
        val updatedSender = sender.copy(friends = sender.friends + receiver)

        userRepository.save(updatedReceiver)
        userRepository.save(updatedSender)
    }

    @Transactional
    fun rejectFriendRequest(receiverId: String, request: FriendRequest) {
        val receiver = findUserByUsername(receiverId)

        val friendRequest = receiver.receivedFriendRequests.find { it.senderId == request.otherId }
            ?: throw UserException(ErrorCodes.REQUEST_NOT_FOUND)

        userRepository.save(receiver.copy(receivedFriendRequests = receiver.receivedFriendRequests - friendRequest))
    }
}


