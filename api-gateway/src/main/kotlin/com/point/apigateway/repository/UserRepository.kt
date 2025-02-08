package com.point.authorization.repository

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository
interface UserRepository  {
    fun loadUserByUsername(username: String?): UserDetails?
}