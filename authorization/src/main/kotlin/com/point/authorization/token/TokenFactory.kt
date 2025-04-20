package com.point.authorization.token

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenFactory(
    @Value("\${jwt.secret.hs512}")
    private val secret: String,
    private val jwsAlgorithm: JWSAlgorithm
) {

    fun create(username: String, type: Type): Token {
        val duration = when(type) {
            Type.REFRESH -> REFRESH_EXPIRATION_TIME
            Type.ACCESS -> ACCESS_EXPIRATION_TIME
        }
        val claimsSet = JWTClaimsSet.Builder()
            .subject(username)
            .issueTime(Date(System.currentTimeMillis()))
            .expirationTime(Date(System.currentTimeMillis() + duration))
            .build()

        val signedJWT = SignedJWT(JWSHeader(jwsAlgorithm), claimsSet)

        signedJWT.sign(MACSigner(secret.toByteArray()))
        return Token(signedJWT.serialize())
    }

    enum class Type {
        REFRESH,
        ACCESS,
    }

    companion object {
        private const val ACCESS_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 1
        private const val REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 31
    }
}