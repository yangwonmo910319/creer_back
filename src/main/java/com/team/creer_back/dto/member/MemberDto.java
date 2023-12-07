package com.team.creer_back.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String image;
    private String address;
    private String phoneNum;
    private String nickName;
}





