package com.point.authorization.security

import com.point.apigateway.SecurityException.TokenExpiredException
import com.point.apigateway.asToken
import com.point.apigateway.isNotExpired
import com.point.apigateway.subject
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val userDetailsService: UserDetailsService) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HEADER_AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith(HEADER_AUTHORIZATION_START)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substringAfter(HEADER_AUTHORIZATION_START).asToken

        if (!token.isNotExpired) {
            SecurityContextHolder.clearContext()
            throw TokenExpiredException
        }

        val userDetails = userDetailsService.loadUserByUsername(token.subject)

        val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authToken

        filterChain.doFilter(request, response)
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_AUTHORIZATION_START = "Bearer "
    }
}