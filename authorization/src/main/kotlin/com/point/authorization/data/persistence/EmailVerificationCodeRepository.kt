package com.point.authorization.data.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, Long> {
    fun findTopByEmailOrderByCreatedAtDesc(email: String): EmailVerificationCode?
}
