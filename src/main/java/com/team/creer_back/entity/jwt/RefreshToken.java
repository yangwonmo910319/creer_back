package com.team.creer_back.entity.jwt;

import com.team.creer_back.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "refresh_token", unique = true) // 고유 값으로 설정
    private String refreshToken;

    @Column(name = "refresh_token_exp")
    private Long refreshTokenExpiresIn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private RefreshToken(String refreshToken, Long refreshTokenExpiresIn, Member member) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.member = member;
    }

    public void update(String refreshToken, Long refreshTokenExpiresIn) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}