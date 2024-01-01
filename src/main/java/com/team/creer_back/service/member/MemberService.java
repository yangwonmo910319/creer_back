package com.team.creer_back.service.member;


import com.team.creer_back.dto.jwt.TokenDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.dto.member.MemberResDto;
import com.team.creer_back.entity.jwt.RefreshToken;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.member.MemberRepository;
import com.team.creer_back.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.team.creer_back.repository.jwt.RefreshTokenRepository;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
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

    // 카카오 가입 여부 확인
    public boolean kakaoSignUpCheck(String kakaoNickName) {
        return memberRepository.existsByNickName(kakaoNickName);
    }

    // 카카오 회원 가입
    public boolean kakaoSignUp(MemberDto memberDto) {
        try {
            Member member = Member.builder()
                    .nickName(memberDto.getNickName())
                    .name("kakaoMember")
                    .email("member@kakao.com")
                    .password("kakaoPassword")
                    .phoneNum("010-0000-0000")
                    .address("kakaoAddress")
                    .build();
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.warn("카카오 회원가입 에러 발생 : " + e);
            return false;
        }
    }

    // 카카오 로그인. 카카오 닉네임으로 사용자 정보를 찾고 토큰 생성 및 반환
    public String kakaoLogin(String kakaoNickName) {
        // 사용자 정보 조회
        Member member = memberRepository.findByNickName(kakaoNickName).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthority());

        // 토큰 생성 및 리프레시 토큰 DB 저장
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken retrievedRefreshToken = refreshTokenRepository
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
            log.info("이미 존재하는 리프레시 토큰: {}", retrievedRefreshToken.getRefreshToken());
            retrievedRefreshToken.update(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn());
        }

        // 토큰 반환
        String token;
        token = String.valueOf(tokenDto);

        return token;
    }
}
