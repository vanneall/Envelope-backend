package com.point.apigateway

import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.text.ParseException

@Component
class JwtUsernameFilter(
    @Value("\${jwt.secret.hs512}")
    private val secret: String,
) : GlobalFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.uri.path

        // Не обрабатывать /auth/**
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange)
        }

        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }

        val token = authHeader.removePrefix("Bearer ").trim()
        val username = getUsernameFromToken(token)

        if (username == null) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }

        val mutatedRequest: ServerHttpRequest = exchange.request.mutate()
            .header("X-User-ID", username)
            .build()

        val mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build()

        return chain.filter(mutatedExchange)
    }

    private fun getUsernameFromToken(token: String): String? = try {
        val signedJWT = SignedJWT.parse(token)
        val verifier = MACVerifier(secret.toByteArray())
        if (!signedJWT.verify(verifier)) null else signedJWT.jwtClaimsSet.subject
    } catch (e: ParseException) {
        null
    } catch (e: JOSEException) {
        null
    }

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}

