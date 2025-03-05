package com.poc.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.poc.chatting.users.repository" //
)
public class JpaConfig {
}

