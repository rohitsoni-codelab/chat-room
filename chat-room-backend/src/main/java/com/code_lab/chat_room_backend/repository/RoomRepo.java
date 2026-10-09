package com.code_lab.chat_room_backend.repository;

import com.code_lab.chat_room_backend.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoomRepo extends MongoRepository<Room,String> {

    Room findByRoomId(String roomId);


}
