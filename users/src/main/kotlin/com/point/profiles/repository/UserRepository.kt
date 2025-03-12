package com.point.profiles.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {

    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<UserEntity>

    @Query(
        """
        SELECT u.* FROM user_friends uf 
        JOIN env_users u ON uf.friend_id = u.username 
        WHERE uf.user_id = :username 
        AND (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) 
        ORDER BY u.name 
        LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    fun findFriendsWithPagination(username: String, name: String?, limit: Int, offset: Int): List<UserEntity>

    @Query(
        """
        SELECT u.* FROM user_blocked ub 
        JOIN env_users u ON ub.blocked_user_id = u.username 
        WHERE ub.user_id = :username 
        AND (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) 
        ORDER BY u.name 
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true
    )
    fun findBlockedUsersWithPagination(username: String, name: String?, limit: Int, offset: Int): List<UserEntity>


    @Query(
        """
        SELECT u.* FROM friend_requests fr 
        JOIN env_users u ON fr.sender_id = u.username 
        WHERE fr.receiver_id = :username 
        ORDER BY fr.id DESC 
        LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    fun findFriendRequestsWithPagination(username: String, limit: Int, offset: Int): List<UserEntity>

    @Query(
        """
        SELECT sender.* 
        FROM friend_requests fr
        JOIN env_users sender ON fr.sender_id = sender.username
        WHERE fr.receiver_id = :username
        ORDER BY fr.id DESC
        LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    fun findIncomingRequests(username: String, limit: Int, offset: Int): List<UserEntity>

    @Query(
        """
        SELECT receiver.* 
        FROM friend_requests fr
        JOIN env_users receiver ON fr.receiver_id = receiver.username
        WHERE fr.sender_id = :username
        ORDER BY fr.id DESC
        LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    fun findOutgoingRequests(username: String, limit: Int, offset: Int): List<UserEntity>


}