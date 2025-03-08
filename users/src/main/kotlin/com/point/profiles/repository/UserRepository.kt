package com.point.profiles.repository

import com.point.profiles.data.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByName(name: String): MutableList<User>
    fun findByNameContaining(name: String): MutableList<User>
}
