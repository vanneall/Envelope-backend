package com.point.apigateway

import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import com.point.authorization.utils.SecurityException
import java.util.*

@JvmInline
value class Token(val value: String)

val Token.isNotExpired: Boolean
    get() {
        val signedJWT = SignedJWT.parse(value)
        return signedJWT.verify(MACVerifier(value.toByteArray())) && signedJWT.jwtClaimsSet.expirationTime.after(Date())
    }

val Token.subject: String?
    get() = SignedJWT.parse(value).jwtClaimsSet.subject

val String.asToken: Token
    get() = try {
        SignedJWT.parse(this)
        Token(this)
    } catch (e: Exception) {
        throw SecurityException.InvalidTokenException
    }

