package com.team.creer_back.controller.member;

import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.service.member.MemberService;
import com.team.creer_back.service.member.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/MyPage")
public class MyPageController {
    private final MemberService memberService;
    private final MyPageService myPageService;

    @Autowired
    public MyPageController(MemberService memberService, MyPageService myPageService) {
        this.memberService = memberService;
        this.myPageService = myPageService;
    }

    // 이미지 업로드
    @PostMapping("/setImage")
    public ResponseEntity<Boolean> setImage(@RequestBody MemberDto memberDto) {
        boolean isTrue = myPageService.setImageUrl(memberDto);
        return ResponseEntity.ok(isTrue);
    }

    // 정보 수정을 위해서 입력 받은 정보들이 존재하는지 확인
    @PostMapping("/checkInfo")
    public boolean checkMemberInfo(@RequestBody MemberDto memberDto) {
        return myPageService.checkMemberInfo(memberDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteMember(@RequestHeader("X-Email") String email) {
        boolean isTrue = myPageService.deleteMember(email);
        return ResponseEntity.ok(isTrue);
    }

    // 회원 수정
    @PutMapping("/modify")
    public ResponseEntity<Boolean> memberModify(@RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getEmail());
        boolean isTrue = memberService.modifyMember(memberDto);
        return ResponseEntity.ok(isTrue);
    }
}