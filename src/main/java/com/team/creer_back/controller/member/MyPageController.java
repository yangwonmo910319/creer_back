package com.team.creer_back.controller.member;

import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberReqDto;
import com.team.creer_back.service.member.MemberService;
import com.team.creer_back.service.member.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    // 토큰에 해당된 사용자의 비밀번호와 클라이언트가 전달한 비밀번호 비교
    @PostMapping("/checkInfo")
    public ResponseEntity<Boolean> checkMemberInfo(@RequestHeader("Authorization") String accessToken, @RequestBody MemberDto memberDto) {
        log.info("memberDto: {}", memberDto.getPassword());
        return ResponseEntity.ok(myPageService.checkMemberInfo(memberDto.getPassword()));
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteMember(@RequestHeader("X-Email") String email) {
        try {
            if (myPageService.deleteMember(email)) {
                log.info("회원 탈퇴 성공");
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.badRequest().body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    // 회원 수정
    @PutMapping("/modify")
    public ResponseEntity<Boolean> memberModify(@RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getEmail());
        boolean isTrue = memberService.modifyMember(memberDto);
        return ResponseEntity.ok(isTrue);
    }

    @PutMapping("/updateName")
    public ResponseEntity<Boolean> updateNickname(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getNickName());
        if (myPageService.updateNickName(memberDto)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PutMapping("/updatePw")
    public ResponseEntity<Boolean> updatePassword(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getPassword());
        if (myPageService.updatePassword(memberDto)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PutMapping("/updatePhone")
    public ResponseEntity<Boolean> updatePhone(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getPhoneNum());
        if (myPageService.updatePhone(memberDto)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PutMapping("/updateAddress")
    public ResponseEntity<Boolean> updateAddress(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getAddress());
        if (myPageService.updateAddress(memberDto)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PutMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDto memberDto) {
        log.info("memberDto: {}", memberDto.getEmail());
        try {
            if (myPageService.updateEmail(memberDto)) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

    }
}