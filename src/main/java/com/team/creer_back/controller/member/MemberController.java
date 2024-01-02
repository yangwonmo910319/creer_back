package com.team.creer_back.controller.member;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.team.creer_back.constant.Authority;
import com.team.creer_back.dto.jwt.TokenDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.dto.member.MemberResDto;
import com.team.creer_back.security.SecurityUtil;
import com.team.creer_back.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberDto memberDto;

    @Autowired
    public MemberController(MemberService memberService, MemberDto memberDto) {
        this.memberService = memberService;
        this.memberDto = memberDto;
    }

    // 회원 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<MemberResDto> memberDetail() {
        Long id = SecurityUtil.getCurrentMemberId();
        MemberResDto memberResDto = memberService.getMemberDetail(id);
        return ResponseEntity.ok(memberResDto);
    }

    // 별명 중복 확인
    @GetMapping("/signUp/nickName/{nickName}")
    public ResponseEntity<Boolean> nickNameExists(@PathVariable String nickName) {
        log.info("nickName : " + nickName);
        boolean isTrue = memberService.isNickName(nickName);
        return ResponseEntity.ok(!isTrue);
    }


    // Kakao
    // 카카오 로그인
    @PostMapping("/kakaoLogin")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestBody Map<String, Object> kakaoData) throws JSONException, JsonProcessingException {
        // @RequestBody : Http 요청 본문을 Map<String, Object> 형태로 파싱하여 kakaoData 라는 변수에 저장하고 있으며, 해당 어노테이션은 Http 요청 본문을 자바 객체로 변환할 때 사용한다.

        // kakaoData에서 "access_token" 이라는 key를 사용하여 카카오 토큰을 추출해서 변수에 저장
        // "access_token" 이라는 이름의 key는 OAuth2.0 인증 방식에서 사용하는 표준 키이다.
        // OAuth 2.0 : 사용자 인증을 위한 일종의 표준 프로토콜로, 카카오, 구글 등 많은 서비스에서 이를 사용하고 있다.
        String kakaoToken = (String) kakaoData.get("access_token");

        // 만약 kakaoToken이 null, 혹은 비어있다면,
        if (kakaoToken == null || kakaoToken.isEmpty()) {
            log.error("MemberController kakaoLogin 에서 kakaoLogin = NULL");
            return null;
        }

        // kakaoToken을 통해 카카오 서버에서 사용자 정보 조회
        String kakaoUserInfo = memberService.requestKakaoUserInfo(kakaoToken);

        // JSON 파싱
        // 카카오 사용자 정보를 JSON 객체로 파싱하여 변수에 저장, 이를 통해 JSON 형식의 데이터를 쉽게 다룰 수 있게 된다.
        JSONObject jsonObject = new JSONObject(kakaoUserInfo);

        // 바로 위의 코드에서 생성한 변수에서 "id" 와 "nickname" 정보를 추출하여 각각 새로운 변수에 저장
        String kakaoNickName = jsonObject.getJSONObject("properties").get("nickname").toString(); // "properties" 와, "nickname" 라는 key의 이름들은 카카오에서 지정한 것
        String kakaoProfileImageUrl = jsonObject.getJSONObject("properties").get("profile_image").toString();


        // 회원 정보 조회 및 처리
        if (!memberService.kakaoSignUpCheck(kakaoNickName)) {
            // 회원 가입 후 로그인 처리
            log.warn("카카오 닉네임 : " + kakaoNickName + "은 가입되어 있지 않습니다.");
            memberDto.setNickName(kakaoNickName);
            memberDto.setAuthority(Authority.ROLE_USER);
            memberDto.setImage(kakaoProfileImageUrl);
            memberService.kakaoSignUp(memberDto);
        }

        // 카카오 닉네임이 이미 가입되어 있는 경우(또는 방금 가입한 경우), 로그인 처리
        TokenDto token = memberService.kakaoLogin(kakaoNickName);

        return ResponseEntity.ok(token);
    }
}