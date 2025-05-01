package com.point.profiles.users.data

import jakarta.persistence.*

@Entity
@Table(
    name = "contacts",
    uniqueConstraints = [UniqueConstraint(columnNames = ["owner_id", "contact_id"])]
)
data class ContactEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    val contact: UserEntity
)
