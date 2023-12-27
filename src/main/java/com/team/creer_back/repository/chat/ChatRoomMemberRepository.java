package com.team.creer_back.repository.chat;


import com.team.creer_back.entity.chat.ChatRoom;
import com.team.creer_back.entity.chat.ChatRoomMember;
import com.team.creer_back.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    ChatRoomMember findByChatRoomAndMember(ChatRoom chatRoom, Member member);
    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);
}
