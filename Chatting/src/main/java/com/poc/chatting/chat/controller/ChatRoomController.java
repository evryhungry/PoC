package com.poc.chatting.chat.controller;

import com.poc.chatting.chat.document.ChatRoom;
import com.poc.chatting.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /** 1. 채팅방 생성 */
    @PostMapping
    public ResponseEntity<ChatRoom> createRoom(@RequestBody String name) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(name));
    }

    /** 2. 모든 채팅방 조회 */
    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        return ResponseEntity.ok(chatRoomService.getAllRooms());
    }

    /** 3. 특정 채팅방 조회 */
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getRoomById(@PathVariable String roomId) {
        return chatRoomService.getRoomById(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** 4. 채팅방 참여 */
    @PostMapping("/{roomId}/join/{userId}")
    public ResponseEntity<ChatRoom> joinRoom(@PathVariable String roomId, @PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.addParticipant(roomId, userId));
    }

    /** 5. 채팅방 퇴장 */
    @PostMapping("/{roomId}/leave/{userId}")
    public ResponseEntity<ChatRoom> leaveRoom(@PathVariable String roomId, @PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.removeParticipant(roomId, userId));
    }

    /** 6. 채팅방 삭제 */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
