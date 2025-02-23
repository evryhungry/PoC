package com.poc.chatting.chat.entity;


import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @UuidGenerator
    private String roomId;
    private String name;

    /**
     * 채팅방 생성 메서드
     * @param name 채팅방 이름
     * @return 생성된 ChatRoom 객체
     */
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString(); // 고유한 roomId 생성
        chatRoom.name = name;
        return chatRoom;
    }
}
