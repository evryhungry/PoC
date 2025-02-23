package com.poc.chatting.chat.controller;

import com.poc.chatting.chat.model.ChatMessage;
import com.poc.chatting.chat.repository.ChatRoomRepository;
import com.poc.chatting.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * WebSocket을 통해 "/pub/chat/message"로 들어오는 메시지를 처리하는 컨트롤러
 */
@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 클라이언트에서 보낸 메시지를 처리하는 메서드
     * <p>
     * WebSocket을 통해 "/pub/chat/message" 경로로 들어온 메시지를 받아 처리합니다.
     * </p>
     *
     * @param message 클라이언트가 전송한 채팅 메시지
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        log.info("Received message: {}", message);

        // 사용자가 채팅방에 입장하는 경우
        if (ChatMessage.Type.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("User {} entered room {}", message.getSender(), message.getRoomId());
        }

        // Redis를 통해 해당 채팅방의 모든 구독자에게 메시지 발행 (Publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}

