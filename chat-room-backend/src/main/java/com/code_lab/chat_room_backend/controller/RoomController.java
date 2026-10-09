package com.code_lab.chat_room_backend.controller;

import com.code_lab.chat_room_backend.entity.Message;
import com.code_lab.chat_room_backend.entity.Room;
import com.code_lab.chat_room_backend.repository.RoomRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {


    private RoomRepo roomRepo;

    public RoomController(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }


    //create room
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomid) {
        if (roomRepo.findByRoomId(roomid) != null) {
            return ResponseEntity.badRequest().body("Room already exists");
        }

        Room room = new Room();
        room.setRoomId(roomid);

        Room savedRoom=roomRepo.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);

    }

    //get room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {
        if (roomRepo.findByRoomId(roomId) == null) {
            return ResponseEntity.badRequest().body("Room Does not Exists");
        }
        return ResponseEntity.ok(roomRepo.findByRoomId(roomId));
    }

    //get message of room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        Room room = roomRepo.findByRoomId(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        List<Message> messages = room.getMessages();

        int start=Math.max(0,messages.size()-(page+1)*size);
        int end=Math.min(messages.size(),(start+size));
        List<Message> paginatedMessage=messages.subList(start,end);

        return ResponseEntity.ok(paginatedMessage);
    }


}
