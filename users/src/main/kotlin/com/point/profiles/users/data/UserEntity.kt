package com.point.profiles.users.data

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "env_users")
data class UserEntity(

    @Id
    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val status: String? = null,

    @Column(nullable = true)
    val about: String? = null,

    @Column(nullable = false)
    val birthDate: LocalDate,

    @Column(nullable = false)
    var lastSeen: LocalDateTime,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_photos", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "photo_id")
    val photos: MutableList<Long> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_contacts",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "contact_id")]
    )
    val contacts: MutableSet<UserEntity> = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_blocked",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "blocked_user_id")]
    )
    val blockedUsers: MutableSet<UserEntity> = mutableSetOf(),
)

