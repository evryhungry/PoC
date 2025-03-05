package com.poc.chatting.chat.service;

import com.poc.chatting.chat.document.ChatMessage;
import com.poc.chatting.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;

    /**
     * ✅ 채팅 메시지 저장 (MongoDB)
     */
    public void saveMessage(ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        log.info("Saved chat message: {}", savedMessage);
    }

    /**
     * ✅ 특정 채팅방의 메시지 조회 (MongoDB)
     */
    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    /**
     * ✅ 채팅 메시지를 Redis에 전송 (WebSocket 실시간 전송)
     */
    public void sendMessage(ChatMessage chatMessage) {
        // MongoDB에 저장
        saveMessage(chatMessage);

        // Redis Pub/Sub을 이용해 실시간 메시지 전달
        redisPublisher.publish(chatRoomService.getTopic(chatMessage.getRoomId()), chatMessage);
    }
}
