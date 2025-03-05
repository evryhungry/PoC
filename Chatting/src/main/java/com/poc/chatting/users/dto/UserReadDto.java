package com.poc.chatting.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poc.chatting.users.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReadDto{
    private Long user_id;
    private String username;
    private String password;

    public UserReadDto(User user) {
        this.user_id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}
