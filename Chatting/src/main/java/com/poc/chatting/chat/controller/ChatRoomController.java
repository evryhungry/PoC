package com.poc.chatting.chat.controller;

import com.poc.chatting.chat.entity.ChatRoom;
import com.poc.chatting.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 채팅방을 조회, 생성, 입장을 담당하는 컨트롤러
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 목록 페이지 반환
     *
     * @param model Spring의 Model 객체
     * @return "/chat/room" 뷰 페이지
     */
    @GetMapping("/room")
    public String rooms(Model model) {
        model.addAttribute("rooms", chatRoomRepository.findAllRoom());
        return "room";  // ✅ "room"을 반환하여 templates/room.html을 찾도록 수정
    }

    /**
     * 특정 채팅방 상세 페이지로 이동
     * @param model Spring의 Model 객체
     * @param roomId 입장할 채팅방의 ID
     * @return "roomdetail" 뷰 페이지
     */
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "roomdetail";  // ✅ "roomdetail"을 반환하여 templates/roomdetail.html을 찾도록 수정
    }

    /**
     * 모든 채팅방 목록을 JSON 형식으로 반환
     * @return List<ChatRoom> 채팅방 목록
     */
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    /**
     * 새로운 채팅방 생성 후 반환
     * @param name 생성할 채팅방의 이름
     * @return 생성된 ChatRoom 객체
     */
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    /**
     * 특정 채팅방 정보를 JSON 형식으로 반환
     * @param roomId 조회할 채팅방의 ID
     * @return ChatRoom 객체
     */
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}

