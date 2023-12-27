package com.team.creer_back.entity.chat;

import com.team.creer_back.entity.member.Member;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "chat_room_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "member_id"})) // room_id 와 member_id의 조합이 Unique
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMember {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
