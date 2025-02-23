package com.poc.chatting.chat.service;

import com.poc.chatting.chat.model.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * RedisPublisher 생성자
     * @param redisTemplate Redis와의 상호작용을 위한 객체
     */
    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 특정 Redis 채널로 채팅 메시지를 발행
     * @param topic 발행할 Redis 채널
     * @param message 전송할 채팅 메시지
     */
    public void publish(ChannelTopic topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
