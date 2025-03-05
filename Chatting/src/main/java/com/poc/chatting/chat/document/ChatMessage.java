package com.poc.chatting.chat.document;

import com.poc.chatting.common.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {
    public enum Type {
        ENTER, TALK
    }

    @Id
    private String id;

    private String roomId;
    private String sender;
    private String message;
    private Type type;
    private Instant timestamp = Instant.now();


}
