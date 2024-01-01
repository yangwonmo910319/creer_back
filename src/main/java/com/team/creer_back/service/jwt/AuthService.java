package com.team.creer_back.service.jwt;

import com.team.creer_back.dto.jwt.TokenDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.dto.member.MemberResDto;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.entity.jwt.RefreshToken;
import com.team.creer_back.repository.member.MemberRepository;
import com.team.creer_back.repository.jwt.RefreshTokenRepository;
import com.team.creer_back.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RefreshTokenRepository refreshTokenRepository;

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
        // 사용자가 입력한 정보를 토대로 토큰을 생성, 인증용 객체라고 생각해도 무방하다.
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        // 이를 이용하여 인증을 시도
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        // 인증 성공시 리프레시 토큰 DB 저장 및 토큰 반환
        // 리프레시 토큰 DB 저장을 위해 사용자 엔티티 조회
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberRepository.findById(Long.valueOf(userDetails.getUsername())).orElseThrow(
                () -> new RuntimeException("해당 유저가 존재하지 않습니다.")
        );

        // 리프레시 토큰 DB 저장, 단 이미 존재하는 경우에는 갱신만 해준다.
        String refreshToken = tokenDto.getRefreshToken();
        RefreshToken retrievedRefreshToken = refreshTokenRepository
                .findByMember(member)
                .orElse(null);
        if (retrievedRefreshToken == null) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .refreshToken(refreshToken)
                    .refreshTokenExpiresIn(tokenDto.getRefreshTokenExpiresIn())
                    .member(member)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        } else {
            log.info("이미 존재하는 리프레시 토큰: {}", retrievedRefreshToken.getRefreshToken());
            retrievedRefreshToken.update(refreshToken, tokenDto.getRefreshTokenExpiresIn());
        }

        return tokenDto;
    }

    // 리프레시 토큰의 유효성을 검증한 후, 유효할 시 새로운 액세스 토큰을 생성해서 반환
    public TokenDto refreshAccessToken(String refreshToken) {
        try {
            if (tokenProvider.validateToken(refreshToken)) {
                TokenDto tokenDto = tokenProvider.generateTokenDto(tokenProvider.getAuthentication(refreshToken));

                // 로그에 TokenDto의 일부 데이터를 출력
                log.info("refreshAccessToken tokenDto 액세스 토큰 : {}", tokenDto.getAccessToken());
                log.info("refreshAccessToken tokenDto 리프레시 토큰 : {}", tokenDto.getRefreshToken());

                RefreshToken retrievedRefreshToken = refreshTokenRepository
                        .findByRefreshToken(refreshToken.substring(0, refreshToken.length() - 1))
                        .orElseThrow(() -> new RuntimeException("해당 리프레시 토큰이 존재하지 않습니다."));
                retrievedRefreshToken.update(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn());

                return tokenDto;
            }
        } catch (RuntimeException e) {
            log.error("refreshAccessToken 에서 토큰 유효성 검증 중 예외 발생: {}", e.getMessage());
        }
        return null;
    }
}
