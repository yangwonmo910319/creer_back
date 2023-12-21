package com.team.creer_back.service.jwt;


import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor

// 스프링 시큐리티에서 제공하는 UserDetailsService 인터페이스를 구현한 클래스로, 사용자의 인증 정보를 불러오는 것이 주 역할
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    // 로그인 시 입력한 이메일을 통해 데이터 베이스에서 회원 정보를 가져와서,
    // createUserDetails() 메서드를 통해 UserDetails 타입으로 변환한다.
    // loadUserByUsername 메소드를 오버라이드 하는데 여기서 넘겨받은 UserDetails 와 Authentication 의 패스워드를 비교하고 검증하는 로직을 처리
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 DB에서 찾을 수 없습니다"));
    }

    // DB에서 가져온 회원 정보를 UserDetails 타입으로 변환한다.
    private UserDetails createUserDetails(Member member) {
        // 권한 정보를 문자열로 변환
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        // UserDetails 타입의 객체를 생성해 리턴
        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}