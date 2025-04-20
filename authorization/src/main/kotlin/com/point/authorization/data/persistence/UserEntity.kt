package com.point.authorization.data.persistence

import com.point.authorization.data.domain.UserInfo
import com.point.authorization.utils.PasswordHasher
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(

    @Id
    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = true, unique = true)
    val email: String? = null,

    @Column(nullable = true, unique = true)
    val phoneNumber: String? = null,

    @Column(nullable = false)
    val password: String
)

fun UserInfo.toEntity(): UserEntity = UserEntity(
    username = username,
    email = null,
    phoneNumber = null,
    password = PasswordHasher.hash(password),
)


