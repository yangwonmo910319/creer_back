package com.team.creer_back.entity.chat;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @Column(name = "room_id")
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDateTime regDate;

    // Q. Set<Member> 과 Set<Long> 중 뭐가 나을까?
    /*
    A. 전자는 Member 엔티티를 직접 참조하기 때문에 직접적인 참조가 가능하며,
    JPA의 연관관계 관리 (지연 로딩 같은)을 사용이 가능하다.
    후자는 아이디만을 저장하기 때문에 성능 향상에 도움이 되나, Member 엔티티의 필드를
    사용하기 위해서는 매번 Member 엔티티를 조회해야 하기 때문에 코드가 복잡해질 수 있다.
    또한 JPA 의 연관관계 관리 기능을 사용할 수 없다.
    */
}
