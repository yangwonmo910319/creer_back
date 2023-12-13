package com.team.creer_back.service.jwt;


import com.team.creer_back.dto.jwt.TokenDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.dto.member.MemberResDto;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.member.MemberRepository;
import com.team.creer_back.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder; // 인증을 담당하는 클래스
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원 가입
    public MemberResDto signup(MemberReqDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        // 이메일 중복을 확인한 후, 중복되지 않을 경우 PasswordEncoder을 사용해 비밀번호를 암호화하고 회원 정보를 저장
        Member member = requestDto.toEntity(passwordEncoder);
        return MemberResDto.of(memberRepository.save(member));
    }

    // 로그인
    public TokenDto login(MemberReqDto requestDto) {
        /*
        사용자가 유효한 경우, TokenProvider를 사용해서 액세스 토큰 과 리프레시 토큰을 생성,
        해당 토큰들은 TokenDto에 담겨서 클라이언트에게 전달된다.
        */
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
    }

    public TokenDto refreshAccessToken(String refreshToken) {
        //refreshToken 검증
        try {
            if (tokenProvider.validateToken(refreshToken)) {
                return tokenProvider.generateTokenDto(tokenProvider.getAuthentication(refreshToken));
            }
        } catch(RuntimeException e) {
            log.error("토큰 유효성 검증 중 예외 발생: {}", e.getMessage());
        }
        return null;
    }
}