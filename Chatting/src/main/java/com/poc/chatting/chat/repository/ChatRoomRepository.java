package com.poc.chatting.chat.repository;

import com.poc.chatting.chat.document.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
}