package com.poc.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.poc.chatting.chat.repository"
)
public class MongoConfig {
}

