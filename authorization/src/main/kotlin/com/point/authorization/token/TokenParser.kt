package com.point.authorization.token

import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import com.point.authorization.error.exceptions.InvalidTokenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TokenParser(
    @Value("\${jwt.secret.hs512}")
    private val secret: String,
) {

    fun getUsername(token: String): String {
        val signedJWT = SignedJWT.parse(token)
        val verifier = MACVerifier(secret.toByteArray())
        return if (signedJWT.verify(verifier)) {
            signedJWT.jwtClaimsSet.subject
        } else {
            throw InvalidTokenException()
        }
    }
}