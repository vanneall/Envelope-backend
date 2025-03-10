package com.point.profiles.repository

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
    val photos: List<Long> = emptyList(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_friends",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "friend_id")]
    )
    val friends: List<UserEntity> = emptyList(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_blocked",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "blocked_user_id")]
    )
    val blockedUsers: List<UserEntity> = emptyList(),

    @OneToMany(mappedBy = "receiverId", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val receivedFriendRequests: List<FriendRequestEntity> = emptyList(),

    @OneToMany(mappedBy = "senderId", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val sentFriendRequests: List<FriendRequestEntity> = emptyList()
) {
    @Entity
    @Table(name = "friend_requests")
    data class FriendRequestEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(name = "sender_id", nullable = false)
        val senderId: String,

        @Column(name = "receiver_id", nullable = false)
        val receiverId: String
    )
}


