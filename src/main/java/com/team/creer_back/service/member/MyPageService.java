package com.team.creer_back.service.member;

import com.team.creer_back.controller.member.MyPageController;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class MyPageService {
    private final MemberRepository memberRepository;
    @Autowired
    public MyPageService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    // 이미지 등록
    @Transactional
    public boolean setImageUrl(MemberDto memberDto) {
        try {
            Member member = memberRepository.findById(memberDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberDto.getId()));
            member.setImage(memberDto.getImage());
            return true;
        }
        catch (Exception e){ // 롤백 발생
            return false;
        }
    }

    // 회원 존재 여부 확인
    public boolean checkMemberInfo(MemberDto memberDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDto.getEmail());
        // "Optional<Member> member" 와 같이 선언할 시, member가 NULL일수도 있기 때문에, getter 메서드를 사용할 수가 없다.

        if (optionalMember.isPresent()) { // optionalMember에 값이 존재한다면 true, 없다면 false 반환
            Member member = optionalMember.get();
            return member.getName().equals(memberDto.getName())
                    && passwordEncoder.matches(encodedPassword, member.getPassword())
                    && member.getPhoneNum().equals(memberDto.getPhoneNum());
        } else {
            return false;
        }
    }

    // 회원 탈퇴
    public void deleteMember(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 회원이 존재하지 않습니다."));
        memberRepository.delete(member);
    }
}
