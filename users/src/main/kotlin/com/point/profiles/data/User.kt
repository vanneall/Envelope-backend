package com.point.profiles.data

import jakarta.persistence.*
import java.util.*

@Entity(name = "envelope_users")
data class User(
    @Id
    val id: UUID,

    val name: String,
    val birthDate: Date,

    @Column(nullable = true)
    val status: String? = null,

    @Temporal(TemporalType.TIMESTAMP)
    var lastSeen: Date = Date(),

    @ElementCollection
    @CollectionTable(name = "user_friends", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "friend_id")
    val friends: MutableList<UUID> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "user_blocked", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "blocked_id")
    val blockedUsers: MutableList<UUID> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "user_photos", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "photo")
    val photos: MutableList<Long> = mutableListOf()
)


