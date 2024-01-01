package com.team.creer_back.repository.jwt;

import com.team.creer_back.entity.member.Member;
import com.team.creer_back.entity.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken); // 리프레시 토큰으로 조회. 액세스 토큰 재발급 시 사용
    Optional<RefreshToken> findByMember(Member member); // 회원으로 조회. 로그인시 사용
}