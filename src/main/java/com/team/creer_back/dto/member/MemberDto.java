package com.team.creer_back.dto.member;

import com.team.creer_back.constant.Authority;
import lombok.*;
import org.springframework.stereotype.Component;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String image;
    private String address;
    private String phoneNum;
    private String nickName;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    // 수정
    @Builder
    public MemberDto(Long id, String image) {
        this.id = id;
        this.image = image;
    }
}





