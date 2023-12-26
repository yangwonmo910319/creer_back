package com.team.creer_back.service.member;

import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.member.MemberRepository;
import com.team.creer_back.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MyPageService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyPageService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 이미지 등록
    @Transactional
    public boolean setImageUrl(MemberDto memberDto) {
        try {
            Member member = memberRepository.findById(memberDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberDto.getId()));
            member.setImage(memberDto.getImage());
            return true;
        } catch (Exception e) { // 롤백 발생
            return false;
        }
    }

    // 회원 존재 여부 확인
    public boolean checkMemberInfo(String password) { // 리액트로부터 받은 문자열 그 자체 '1q2w3e4r'
        // 현재 로그인한 사용자의 정보를 가져온다.
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) { // optionalMember에 값이 존재한다면 true, 없다면 false 반환
            Member member = optionalMember.get();
            return passwordEncoder.matches(password, member.getPassword()); // 문자열 그 자체(1q2w3e4r)와 암호화된 문자열을 비교
        } else {
            return false;
        }
    }

    // 회원 탈퇴
    @Transactional
    public boolean deleteMember(String email) {
        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 회원이 존재하지 않습니다."));
            memberRepository.delete(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 닉네임 변경
    @Transactional
    public boolean updateNickName(MemberReqDto reqDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        // 토큰을 통해 사용자 정보를 찾고 닉네임 중복이 아니면 닉네임 변경
        if (optionalMember.isPresent() && !memberRepository.existsByNickName(reqDto.getNickName())) {
            Member member = optionalMember.get();
            member.setNickName(reqDto.getNickName());
            return true;
        }
        return false;
    }

    // 비밀번호 변경
    @Transactional
    public boolean updatePassword(MemberReqDto reqDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            member.setPassword(passwordEncoder.encode(reqDto.getPassword()));
            return true;
        }
        return false;
    }

    // 전화번호 변경
    @Transactional
    public boolean updatePhone(MemberReqDto reqDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setPhoneNum(reqDto.getPhoneNum());
            return true;
        }
        return false;
    }

    // 주소 변경
    @Transactional
    public boolean updateAddress(MemberReqDto reqDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setAddress(reqDto.getAddress());
            return true;
        }
        return false;
    }

    // 이메일 변경
    @Transactional
    public boolean updateEmail(MemberReqDto reqDto) {
        // 이메일 중복 확인
        if (memberRepository.existsByEmail(reqDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Long memberId = SecurityUtil.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setEmail(reqDto.getEmail());
            return true;
        }
        return false;
    }
}