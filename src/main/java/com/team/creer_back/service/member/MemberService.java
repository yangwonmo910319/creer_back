package com.team.creer_back.service.member;


import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.dto.member.MemberResDto;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.member.MemberRepository;
import com.team.creer_back.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 회원 가입 여부 확인
    public boolean isMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원 상세 조회
    public MemberResDto getMemberDetail(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );
        log.info(member.getAddress());
        log.info(member.getName());
        log.info(member.getNickName());
        log.info(member.getImage());
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

    // 회원 삭제
    public boolean deleteMember(String email) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            memberRepository.delete(member);
            return true; // 회원이 존재하면 true 반환
        } catch (RuntimeException e) {
            return false; // 회원이 존재하지 않으면 false 반환
        }
    }

    // 회원 전체 조회
    public List<MemberResDto> getMemberList() {
        List<Member> members = memberRepository.findAll();
        List<MemberResDto> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(convertEntityToDto(member));
        }
        return memberDtos;
    }

    // 총 페이지수
    public int getMemberPage(Pageable pageable) {

        return memberRepository.findAll(pageable).getTotalPages();
    }

    // 회원 조회 : 페이지 네이션
    public List<MemberResDto> getMemberList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Member> members = memberRepository.findAll(pageable).getContent();
        List<MemberResDto> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(convertEntityToDto(member));
        }
        return memberDtos;
    }

    // 회원 엔티티를 회원 DTO로 변환
    private MemberResDto convertEntityToDto(Member member) {
        MemberResDto memberDto = new MemberResDto();
        memberDto.setEmail(member.getEmail());
        memberDto.setName(member.getName());
        memberDto.setImage(member.getImage());
        memberDto.setNickName(member.getNickName());
        memberDto.setAddress(member.getAddress());
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

    // 카카오 로그인
    public String kakaoLogin(String kakaoNickName) {
        // 사용자 정보 조회
        Member member = memberRepository.findByNickName(kakaoNickName).orElse(null);

        // 인증 객체 생성
        assert member != null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthority());

        // 토큰 생성
        String token;
        token = String.valueOf(tokenProvider.generateTokenDto(authentication));

        return token;
    }
}
