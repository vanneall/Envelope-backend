package com.point.chats.events.rest

import org.springframework.http.server.ServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

class CustomHandshakeHandler : DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal {
        val userId = attributes["userId"] as? String ?: "anonymous"
        // Сделаем свой класс Principal
        return StompPrincipal(userId)
    }
}

// Любой класс, реализующий java.security.Principal
data class StompPrincipal(val userId: String) : Principal {
    override fun getName(): String = userId
}
