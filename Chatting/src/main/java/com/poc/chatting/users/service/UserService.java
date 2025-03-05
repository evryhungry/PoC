package com.poc.chatting.users.service;

import com.poc.chatting.users.dto.UserCreateDto;
import com.poc.chatting.users.dto.UserReadDto;
import com.poc.chatting.users.entity.User;
import com.poc.chatting.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(UserCreateDto dto) {
        userRepository.save(User.builder()
                .username(dto.getUsername())
                .build()
        );
    }

    public UserReadDto findById(Long id){
        return new UserReadDto(userRepository.findById(id));
    }

}
