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

    fun create(username: String): Token {
        val claimsSet = JWTClaimsSet.Builder()
            .subject(username)
            .issueTime(Date(System.currentTimeMillis()))
            .expirationTime(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .build()

        val signedJWT = SignedJWT(JWSHeader(jwsAlgorithm), claimsSet)

        signedJWT.sign(MACSigner(secret.toByteArray()))
        return Token(signedJWT.serialize())
    }

    companion object {
        private const val EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 182
    }
}