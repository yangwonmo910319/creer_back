package com.team.creer_back.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.creer_back.constant.Authority;
import com.team.creer_back.dto.jwt.TokenDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.dto.member.MemberResDto;
import com.team.creer_back.entity.jwt.RefreshToken;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.jwt.RefreshTokenRepository;
import com.team.creer_back.repository.member.MemberRepository;
import com.team.creer_back.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.objectMapper = objectMapper;
    }

    // 회원 가입 여부 확인
    public boolean isMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원 상세 조회
    public MemberResDto getMemberDetail(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );
        return convertEntityToDto(member);
    }

    // 회원 수정
    public boolean modifyMember(MemberReqDto memberDto) {
        try {
            Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            member.setName(memberDto.getName());
            member.setImage(memberDto.getImage());
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원 엔티티를 회원 DTO로 변환
    private MemberResDto convertEntityToDto(Member member) {
        MemberResDto memberDto = new MemberResDto();
        memberDto.setId(member.getId());
        memberDto.setEmail(member.getEmail());
        memberDto.setName(member.getName());
        memberDto.setImage(member.getImage());
        memberDto.setNickName(member.getNickName());
        memberDto.setAddress(member.getAddress());
        memberDto.setPhoneNum(member.getPhoneNum());
        memberDto.setPassword(member.getPassword());
        return memberDto;
    }

    // 별명 중복 확인
    public boolean isNickName(String nickName) {
        return memberRepository.existsByNickName(nickName);
    }

    // Kakao
    // 카카오 회원 가입 여부 확인
    public boolean kakaoSignUpCheck(String kakaoNickName) {
        return memberRepository.existsByNickName(kakaoNickName);
    }

    // 카카오 회원 가입
    public boolean kakaoSignUp(MemberDto memberDto) {
        try {
            Member member = Member.builder()
                    .nickName(memberDto.getNickName())
                    .name("카카오")
                    .email("member@kakao.com")
                    .password("kakaoPassword")
                    .phoneNum("010-0000-0000")
                    .address("kakaoAddress")
                    .authority(memberDto.getAuthority())
                    .image(memberDto.getImage())
                    .build();
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.warn("카카오 회원가입 에러 발생 : " + e);
            return false;
        }
    }

    // 카카오 서버에서부터 사용자 정보 조회
    public String requestKakaoUserInfo(String kakaoToken) {
        // 사용자 정보를 조회하기 위한 지정 URL
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";

        // HTTP 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoToken);

        // RestTemplate : 외부 API를 호출하기 위해 스프링에서 제공하는 HTTP 클라이언트 유틸리티로,
        // HTTP 요청을 쉽게 보낼 수 있도록 도와주며, 응답 또한 받을 수 있다. (exchange)
        // HTTP 헤더 설정, 요청 파라미터 설정, 에러 핸들링 등의 다양한 기능을 제공한다.
        // 일반적으로 JPA를 이용해 데이터 베이스에 접근하는 API를 만들때는 사용되지 않는다.
        RestTemplate restTemplate = new RestTemplate();

        // 카카오 API에 GET 요청을 보낸다.
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // String.class : 메서드의 마지막 변수로, responseType에 대한 인자로, API 응답의 본문을 String 타입으로 변환하려고 시도한다. 즉, API의 응답이 JSON 형태라면, 이를 String으로 변환하여 반환한다.

        log.warn("카카오 API 응답 : " + responseEntity.getBody());
        return responseEntity.getBody();
    }

    // 카카오 로그인. 카카오 닉네임으로 사용자 정보를 찾고 토큰 생성 및 반환
    public TokenDto kakaoLogin(String kakaoNickName) throws JsonProcessingException {
        Member member = memberRepository.findByNickName(kakaoNickName).orElseThrow(
                () -> new RuntimeException("카카오 별명으로 조회되는 회원 별명이 존재하지 않습니다.")
        );

        // 카카오 회원 정보를 인증 객체로 변환
        String memberId = member.getId().toString(); // 또는 member.getUsername();
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, "", member.getAuthority());

        // 해당 인증 객체를 통해 액세스 및 리프레시 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken retrievedRefreshToken = refreshTokenRepository // 유효한 리프레시 토큰이 존재한다면 else문으로 이동
                .findByMember(member)
                .orElse(null);
        if (retrievedRefreshToken == null) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .refreshToken(tokenDto.getRefreshToken())
                    .refreshTokenExpiresIn(tokenDto.getRefreshTokenExpiresIn())
                    .member(member)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        } else {
            log.warn("MemberService kakaoLogin, 해당 회원은 리프레시 토큰을 발급받은 기록이 있습니다 : {}", retrievedRefreshToken.getRefreshToken());
            retrievedRefreshToken.update(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn());
        }

        log.warn("MemberService kakaoLogin 반환되는 객체 찍어보기 : " + objectMapper.writeValueAsString(tokenDto));
        return tokenDto;
    }
}
