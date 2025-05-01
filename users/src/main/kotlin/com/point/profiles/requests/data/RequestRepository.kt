package com.point.profiles.requests.data

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RequestRepository : JpaRepository<RequestEntity, Long> {

    fun existsByProducer_UsernameAndConsumer_Username(producerUsername: String, consumerUsername: String): Boolean

    @Query(
        """
        SELECT fr 
        FROM RequestEntity fr 
        WHERE fr.consumer.username = :username
        ORDER BY fr.id DESC
        """
    )
    fun findIncomingRequests(@Param("username") username: String, pageable: Pageable): List<RequestEntity>

    @Query("""
    SELECT fr 
    FROM RequestEntity fr 
    WHERE fr.producer.username = :username
    ORDER BY fr.id DESC
""")
    fun findOutgoingRequests(
        @Param("username") username: String,
        pageable: Pageable
    ): List<RequestEntity>

    @Query("""
    SELECT fr.consumer.username
    FROM RequestEntity fr
    WHERE fr.producer.username = :producerUsername
      AND fr.consumer.username IN :targetUsernames
""")
    fun findSentRequestUsernames(
        @Param("producerUsername") producerUsername: String,
        @Param("targetUsernames") targetUsernames: Collection<String>
    ): List<String>

    @Query("SELECT c.contact.username FROM ContactEntity c WHERE c.owner.username = :username")
    fun findContactUsernamesByOwner(username: String): List<String>
}