package com.team.creer_back.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    // 생성자를 private 으로 선언 : 해당 클래스가 유틸 클래스 ( = 정적 메서드만을 가진 클래스)라는 것을 나타낸다.
    // 유틸리티 클래스는 인스턴스화 필요 X
    // 만약 기본 생성자를 private으로 선언하면, 해당 클래스의 객체를 다른 클래스에서 생성이 가능하다.
    // 이를 방지하기 위해서, private 타입으로 생성자를 명시적으로 선언해 놓는 것이다.
    private SecurityUtil() {
    }

    // 인증에 성공한 사용자의 아이디를 호출
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        return Long.parseLong(authentication.getName());
    }
}