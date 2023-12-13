package com.team.creer_back.dto.member;

import com.team.creer_back.constant.Authority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Component
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
}





