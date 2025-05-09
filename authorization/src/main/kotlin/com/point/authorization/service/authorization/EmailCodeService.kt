package com.point.authorization.service.authorization

import com.point.authorization.data.persistence.EmailVerificationCode
import com.point.authorization.data.persistence.EmailVerificationCodeRepository
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EmailCodeService(
    private val repository: EmailVerificationCodeRepository,
    private val mailSender: JavaMailSender
) {
    fun sendCode(email: String) {
        val code = (1000..9999).random().toString()
        val entity = EmailVerificationCode(email = email, code = code)
        repository.save(entity)

        val message = SimpleMailMessage().apply {
            setTo(email)
            subject = "Код подтверждения"
            text = "Ваш код подтверждения: $code"
        }
        mailSender.send(message)
    }

    fun verifyCode(email: String, inputCode: String): Boolean {
        val latest = repository.findTopByEmailOrderByCreatedAtDesc(email)
            ?: return false

        val notExpired = latest.createdAt.isAfter(LocalDateTime.now().minusMinutes(10))
        return notExpired && latest.code == inputCode
    }
}
