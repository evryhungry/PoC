package com.poc.chatting.chat.service;

import com.poc.chatting.chat.document.ChatRoom;
import com.poc.chatting.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final Map<String, ChannelTopic> topics = new HashMap<>();

    /**
     * ✅ 1. 채팅방 생성 (MongoDB에 저장)
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = new ChatRoom(name);
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        log.info("Created chat room: {}", savedRoom);
        return savedRoom;
    }

    /**
     * ✅ 2. 모든 채팅방 조회
     */
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    /**
     * ✅ 3. 특정 채팅방 조회
     */
    public Optional<ChatRoom> getRoomById(String roomId) {
        return chatRoomRepository.findById(roomId);
    }

    /**
     * ✅ 4. 채팅방에 참여자 추가
     * @param roomId 채팅방 ID
     * @param userId 참여자 ID
     * @return 업데이트된 ChatRoom 객체
     */
    public ChatRoom addParticipant(String roomId, String userId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new RuntimeException("Chat room not found with ID: " + roomId);
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        if (!chatRoom.getParticipants().contains(userId)) {
            chatRoom.getParticipants().add(userId);
            chatRoomRepository.save(chatRoom);
            log.info("User {} joined ChatRoom {}", userId, roomId);
        }
        return chatRoom;
    }

    /**
     * ✅ 5. 채팅방에서 참여자 제거
     * @param roomId 채팅방 ID
     * @param userId 제거할 참여자 ID
     * @return 업데이트된 ChatRoom 객체
     */
    public ChatRoom removeParticipant(String roomId, String userId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new RuntimeException("Chat room not found with ID: " + roomId);
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        if (chatRoom.getParticipants().contains(userId)) {
            chatRoom.getParticipants().remove(userId);
            chatRoomRepository.save(chatRoom);
            log.info("User {} left ChatRoom {}", userId, roomId);
        }
        return chatRoom;
    }

    /**
     * ✅ 6. 채팅방 삭제
     * @param roomId 삭제할 채팅방 ID
     */
    public void deleteChatRoom(String roomId) {
        chatRoomRepository.deleteById(roomId);
        log.info("Deleted ChatRoom with ID: {}", roomId);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
