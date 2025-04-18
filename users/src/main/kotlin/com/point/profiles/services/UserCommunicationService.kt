package com.point.profiles.services

import com.point.profiles.exceptions.ErrorCodes
import com.point.profiles.exceptions.UserException
import com.point.profiles.repository.UserEntity
import com.point.profiles.repository.UserRepository
import com.point.profiles.rest.v2.request.BlockedUserRequest
import com.point.profiles.rest.v2.request.FriendRequest
import com.point.profiles.rest.v2.response.OtherUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserCommunicationService(private val userRepository: UserRepository) {

    private fun findUserByUsername(username: String): UserEntity =
        userRepository.findById(username).orElseThrow { UserException(ErrorCodes.USER_NOT_FOUND) }

    @Transactional
    fun getUserContacts(username: String, name: String?, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findFriendsWithPagination(
            username = username,
            name = name,
            limit = limit,
            offset = offset,
        ).map { it.toOtherUserResponse(username) }

    @Transactional
    fun deleteUserFriend(userId: String, deletedUserId: String) {
        val user = findUserByUsername(userId)
        val deletedUser = findUserByUsername(deletedUserId)
        userRepository.save(
            user.copy(friends = user.friends.filter { it.username != deletedUserId })
        )
        userRepository.save(
            deletedUser.copy(friends = deletedUser.friends.filter { it.username != userId })
        )
    }

    @Transactional
    fun getUserBlocked(username: String, name: String?, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findBlockedUsersWithPagination(
            username = username,
            name = name,
            limit = limit,
            offset = offset,
        ).map { it.toOtherUserResponse(username) }

    fun getFriendRequests(username: String, incoming: Boolean, limit: Int, offset: Int): List<OtherUserResponse> =
        if (incoming) {
            getIncomingFriendRequests(username, limit, offset)
        } else {
            getOutgoingFriendRequests(username, limit, offset)
        }

    @Transactional
    protected fun getIncomingFriendRequests(username: String, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findIncomingRequests(username, limit, offset)
            .map { it.toOtherUserResponse(username) }

    @Transactional
    protected fun getOutgoingFriendRequests(username: String, limit: Int, offset: Int): List<OtherUserResponse> =
        userRepository.findOutgoingRequests(username, limit, offset)
            .map { it.toOtherUserResponse(username) }

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

        val updatedUser = user.copy(
            blockedUsers = user.blockedUsers + blockedUser,
            friends = user.friends.filter { it.username != blockedUser.username },
        )
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

        if (sender in receiver.friends) {
            throw UserException(ErrorCodes.USER_ALREADY_FRIEND)
        }

        if (sender.sentFriendRequests.any { it.receiver.username == receiver.username }) {
            throw UserException(ErrorCodes.REQUEST_ALREADY_SENT)
        }

        val newRequest = UserEntity.FriendRequestEntity(
            sender = sender,
            receiver = receiver
        )

        val updatedSender = sender.copy(
            sentFriendRequests = sender.sentFriendRequests + newRequest
        )

        userRepository.save(updatedSender)
    }


    @Transactional
    fun approveFriendRequest(receiverId: String, request: FriendRequest) {
        val receiver = findUserByUsername(receiverId)
        val sender = findUserByUsername(request.otherId)

        if (sender in receiver.friends) {
            throw UserException(ErrorCodes.USER_ALREADY_FRIEND)
        }

        val updatedReceiver = receiver.copy(
            friends = receiver.friends + sender,
            receivedFriendRequests = receiver.receivedFriendRequests.filter { it.sender.username != sender.username },
        )
        val updatedSender = sender.copy(
            friends = sender.friends + receiver,
            sentFriendRequests = sender.sentFriendRequests.filter { it.receiver.username != receiver.username },
        )

        userRepository.save(updatedReceiver)
        userRepository.save(updatedSender)
    }

    @Transactional
    fun rejectFriendRequest(receiverId: String, request: FriendRequest) {
        val receiver = findUserByUsername(receiverId)

        val friendRequest = receiver.receivedFriendRequests.find { it.sender.username == request.otherId }
            ?: throw UserException(ErrorCodes.REQUEST_NOT_FOUND)

        userRepository.save(receiver.copy(receivedFriendRequests = receiver.receivedFriendRequests - friendRequest))
    }
}


