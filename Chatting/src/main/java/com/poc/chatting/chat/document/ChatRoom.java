package com.poc.chatting.chat.document;

import com.poc.chatting.common.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {
    @Id
    private String id;

    private String name;
    private List<String> participants;

    public ChatRoom(String name) {
        this.name = name;
    }
}
