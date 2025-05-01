package com.point.profiles.contacts.repository

import com.point.profiles.users.data.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContactsRepository : JpaRepository<UserEntity, String> {

    @Query("""
    SELECT c.contact
    FROM UserEntity u
    JOIN u.contacts c
    WHERE u.username = :username
      AND LOWER(c.contact.name) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY c.contact.name ASC
    """)
    fun findContactsFiltered(
        @Param("username") username: String,
        @Param("query") query: String,
        pageable: Pageable,
    ): List<UserEntity>
}