package com.point.profiles.users.data

import com.point.profiles.requests.errors.exceptions.RequestNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {
    @Query("""
    SELECT u FROM UserEntity u
    WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY u.name ASC
    """)
    fun findByNameLikeIgnoreCase(@Param("query") query: String, pageable: Pageable): Page<UserEntity>

    @Query("""
    SELECT c.contact
    FROM ContactEntity c
    WHERE c.owner.username = :username
""")
    fun findContactsOfUser(@Param("username") username: String): List<UserEntity>
}

fun UserRepository.findByIdOrThrow(id: String): UserEntity = findById(id).orElseThrow { RequestNotFoundException() }