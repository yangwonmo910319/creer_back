package com.team.creer_back.repository.chat;


import com.team.creer_back.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> { }