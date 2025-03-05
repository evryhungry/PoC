package com.poc.chatting.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.chatting.chat.document.ChatMessage;
import com.poc.chatting.chat.repository.ChatMessageRepository;
import com.poc.chatting.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository cmRepository;

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
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

            if (publishMessage == null) {
                log.warn("Received null message from Redis.");
                return;
            }


            log.info("Received message: {}", publishMessage); // 수신된 메시지 로깅

            // 문자열 메시지를 ChatMessage 객체로 역직렬화

            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            cmRepository.save(roomMessage);

            // 메시지가 정상적으로 변환되었는지 확인
            if (roomMessage.getRoomId() == null || roomMessage.getSender() == null) {
                log.warn("Invalid ChatMessage received: {}", publishMessage);
                return;
            }

            // WebSocket 구독자에게 메시지 전송
            String destination = String.format("/sub/chat/room/%s", roomMessage.getRoomId());
            messagingTemplate.convertAndSend(destination, roomMessage);

        } catch (IOException e) {
            log.error("JSON deserialization error: {}", e.getMessage()); // JSON 변환 오류 로그
        } catch (Exception e) {
            log.error("Unexpected error while processing Redis message: {}", e.getMessage()); // 기타 예외 처리
        }
    }

}

