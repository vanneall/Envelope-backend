package com.point.chats.events.rest

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.text.ParseException

class JwtHandshakeInterceptor : HandshakeInterceptor {

    private val secret = "f85c2a0a2774f388dac963a4a2e42ca4377b5ec15f59271de8b1c4d19c9e98a4509d4034a29357926cf37fabe3b3d687940907e205b50431a0e0665a200b1da49d25e322014ddd6c156c4a39907ba00603a669b992eeee0eafa770c72ca93b8492d13584bc60895260afa1987553dc715e20196b737ef5cdb26664e865d14e834c9288de16a6cfbdb3319a10bde988db427d133775cb2972a1b4499d5ded42948bb8a9643c5a1a679578316639370e8c3e29b7a0e9e0df29bcd986695809e41b212f03f79f76665824a5f181673010e1d66b41c4245dbe25183c20c3a6803fe6971981db9572daf3d216d07d918ab40addef82e672755b82744beb38eb18957f9973c9bccba87fc545617d04a0d15118112092b5b127fa278a03e919058b85ee6512e86143e6cb22bf1f11734b16ce8c19f6e524cd51659a3bd8571bc06dc2374685b95e202edd16c2a36308f8a6ae4db1b070becd81e9d7da79059c115c2e6c8eb91689e0a60ad15aa466197c45a89bb91840e5683d74b689758312903a5dea3a483f644efca40406125ed15bbd3ebbba1b4da03373504badd62db023ab56ff22857b781fb991278d6d2febecb0656325a1f6cc0bb09e60025d8a312aed400dcdcee6a648e25bced22eae8730dd34a63905b613c643422c9a5ea507f3b4ac377467d48f9d5665a97d06f18f5a42e322216f184a4663f5581db8dfee84bbfe50"

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val authHeader = request.headers["Authorization"]?.firstOrNull() // "Bearer xxx"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring("Bearer ".length)
            try {
                val userId = getUserIdFromToken(token) ?: throw JOSEException()
                attributes["userId"] = userId
            } catch (e: Exception) {
                return false
            }
        } else {
            return false
        }
        return true
    }

    private fun getUserIdFromToken(token: String): String? {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val verifier = MACVerifier(secret.toByteArray())
            if (!signedJWT.verify(verifier)) return null
            signedJWT.jwtClaimsSet.subject
        } catch (e: ParseException) {
            null
        } catch (e: JOSEException) {
            null
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: java.lang.Exception?
    ) {
    }
}
