package com.poc.chatting.users.controller;

import com.poc.chatting.users.dto.UserCreateDto;
import com.poc.chatting.users.dto.UserReadDto;
import com.poc.chatting.users.repository.UserRepository;
import com.poc.chatting.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto dto) {
        try {
            userService.save(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getUserByStudentId(@RequestParam("id") Long id) {
        try{
            UserReadDto userReadDto = userService.findById(id);
            return new ResponseEntity<>(userReadDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
