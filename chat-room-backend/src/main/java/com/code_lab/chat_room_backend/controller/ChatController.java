package com.code_lab.chat_room_backend.controller;


import com.code_lab.chat_room_backend.dto.MessageRequest;
import com.code_lab.chat_room_backend.entity.Message;
import com.code_lab.chat_room_backend.entity.Room;
import com.code_lab.chat_room_backend.repository.RoomRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin("*")
public class ChatController {

    private RoomRepo roomRepo;

    public ChatController(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }


    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public Message sendMessage(@DestinationVariable String roomId
                                         , MessageRequest request)
    {

        Message message=new Message();
        Room room=roomRepo.findByRoomId(request.getRoomId());
        message.setContent(request.getContent());
        message.setSender(request.getSenderId());

        if(room==null)
        {
            throw new RuntimeException("Room not exist");
        }
        room.getMessages().add(message);
        roomRepo.save(room);
        return message;
    }
}
