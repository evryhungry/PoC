package com.poc.chatting.chat.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessage {

    public enum Type {
        ENTER, TALK
    }

    private Type type;
    private String message;
    private String sender;
    private String roomId;

}
