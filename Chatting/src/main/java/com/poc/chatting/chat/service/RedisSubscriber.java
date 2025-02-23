package com.poc.chatting.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.chatting.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * Redis에서 발행된 메시지를 수신하는 클래스
 * <p>
 * Redis의 Pub/Sub 기능을 활용하여 특정 채널에 대한 구독(Subscribe)을 수행하며,
 * 수신한 메시지를 WebSocket을 통해 구독자들에게 전달합니다.
 * </p>
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

    /** JSON 데이터를 객체로 변환하기 위한 ObjectMapper */
    private final ObjectMapper objectMapper;

    /** Redis의 데이터 직렬화 및 역직렬화를 담당하는 RedisTemplate */
    private final RedisTemplate redisTemplate;

    /** WebSocket을 통해 메시지를 클라이언트에게 전송하는 SimpMessageSendingOperations */
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(Publish)되었을 때 호출되는 메서드
     *
     * @param message 수신한 메시지 (Redis에서 발행된 데이터)
     * @param pattern 패턴 (사용되지 않지만 인터페이스 요구사항으로 포함됨)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 수신한 메시지를 문자열로 변환
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            log.info("Received message: {}", publishMessage); // 수신된 메시지 로깅

            // 문자열 메시지를 ChatMessage 객체로 역직렬화
            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            // WebSocket 구독자에게 메시지 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage()); // 에러 발생 시 로그 출력
        }
    }
}

