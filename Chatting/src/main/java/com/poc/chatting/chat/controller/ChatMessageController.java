package com.poc.chatting.chat.controller;

import com.poc.chatting.chat.document.ChatMessage;
import com.poc.chatting.chat.service.ChatMessageService;
import com.poc.chatting.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic chatTopic;

    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(chatMessageService.getMessagesByRoomId(roomId));
    }

    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        redisPublisher.publish(chatTopic, chatMessage);
    }
}
