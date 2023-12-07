package com.team.creer_back.entity.member;

import com.team.creer_back.constant.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phoneNum", nullable = false)
    private String phoneNum;
    @Column(name = "nickName", nullable = false)
    private String nickName;
    @Column(unique = true)
    private String email;
    private String image;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String name, String password, String email, String image, String address, String phoneNum, String nickName, Authority authority) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.image = image;
        this.address =address;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.authority = authority;
    }
}