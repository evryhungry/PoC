package com.poc.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    /**
     * 메시지 브로커를 구성합니다.
     * 애플리케이션 내부에서 사용할 경로와 브로커를 통한 경로를 설정합니다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 구독 요청을 처리하는 브로커의 접두사
        registry.setApplicationDestinationPrefixes("/pub"); // 발행 요청의 접두사
    }
    /**
     * STOMP(WebSocket) 엔드포인트를 등록합니다.
     * 클라이언트는 이 엔드포인트를 통해 서버와 WebSocket 연결을 수립할 수 있습니다.
     * SockJS를 지원하여 WebSocket을 지원하지 않는 브라우저에서도Fallback 옵션을 제공합니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp") // WebSocket 연결 주소
                .setAllowedOriginPatterns("*") // 모든 도메인 허용
                .withSockJS(); // SockJS 사용
    }
}
