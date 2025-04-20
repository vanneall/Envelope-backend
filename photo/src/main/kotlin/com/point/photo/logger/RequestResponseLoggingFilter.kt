package com.point.photo.logger

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class RequestResponseLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val start = System.currentTimeMillis()

        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val duration = System.currentTimeMillis() - start
            val method = request.method
            val uri = request.requestURI
            val status = response.status
            val body = String(wrappedResponse.contentAsByteArray)

            logger.info("[$method] $uri → $status in ${duration}ms")
            logger.debug("Response body: $body")

            wrappedResponse.copyBodyToResponse()
        }
    }

    companion object {
        private val logger by lazy { LoggerFactory.getLogger(RequestResponseLoggingFilter::class.java) }
    }
}
