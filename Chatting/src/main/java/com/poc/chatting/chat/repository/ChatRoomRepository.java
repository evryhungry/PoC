package com.poc.chatting.chat.repository;

import com.poc.chatting.chat.entity.ChatRoom;
import com.poc.chatting.chat.service.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ChatRoomRepository {
    // Redis 메시지 리스너 컨테이너
    private final RedisMessageListenerContainer redisMessageListener;
    // Redis 구독자
    private final RedisSubscriber redisSubscriber;
    // RedisTemplate 객체
    private final RedisTemplate<String, Object> redisTemplate;
    // Redis의 HashOperations 객체
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방 토픽 정보를 저장하는 맵
    private Map<String, ChannelTopic> topics;

    // Redis에 저장될 채팅방 정보의 키
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    // 의존성 주입 후 초기화 메서드
    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
        log.info("ChatRoomRepository initialized.");
    }

    /**
     * 모든 채팅방 조회
     * @return 채팅방 리스트
     */
    public List<ChatRoom> findAllRoom() {
        log.info("Fetching all chat rooms.");
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    /**
     * 특정 채팅방 조회
     * @param id 채팅방 ID
     * @return 채팅방 객체
     */
    public ChatRoom findRoomById(String id) {
        log.info("Fetching chat room with id={}", id);
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성
     * @param name 채팅방 이름
     * @return 생성된 채팅방 객체
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        log.info("Created chat room with id={} and name={}", chatRoom.getRoomId(), chatRoom.getName());
        return chatRoom;
    }

    /**
     * 채팅방 입장 처리
     * @param roomId 채팅방 ID
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
            log.info("Created and stored new topic for roomId={}", roomId);
        } else {
            log.info("Topic already exists for roomId={}", roomId);
        }
    }

    /**
     * 채팅방의 토픽 조회
     * @param roomId 채팅방 ID
     * @return 채널 토픽
     */
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}