package com.point.chats.events.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic", "/queue") // Теперь есть отдельная очередь для отправителя
        config.setApplicationDestinationPrefixes("/app") // Клиенты отправляют сюда
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws") // Эндпоинт WebSocket
            .setAllowedOriginPatterns("*")
            .withSockJS() // Поддержка SockJS для браузеров без WebSocket
    }
}
