package com.team.creer_back.controller.member;


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
    // 카카오 로그인 상태 확인
    @GetMapping("/checkKakaoLogin")
    public ResponseEntity<String> checkKakaoLogin(@RequestHeader("Authorization") String authorizationHeader) {
        // Authorization 라는 이름의 요청 헤더를 가져와서 authorizationHeader 라는 String 변수에 저장

        // "Bearer" 라는 접두어를 제거하고 실제 토큰 값을 변수에 저장
        String token = authorizationHeader.substring("Bearer ".length());

        // 카카오 API에 요청을 보낼 URL을 requestUrl에 저장
        // 해당 URL은 카카오가 제공하는 토큰 확인 API의 주소로, 이 주소로 HTTP 요청을 보내면, 해당 토큰에 대한 사용자 정보를 확인이 가능하다.
        final String requestUrl = "https://kapi.kakao.com/v1/user/access_token_info";

        // HTTP 헤더 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        // RestTemplate 객체를 생성
        // exchange 메서드를 사용하여 카카오 API 에 GET 요청을 보내는데, 이때 요청 헤더에는 위에서 생성한 Http 헤더 객체를 사용한다.
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>("이미 카카오 아이디로 로그인한 상태입니다.", HttpStatus.OK); // 상태 코드 : 200
        } else {
            return new ResponseEntity<>("현재 카카오 아이디로 로그인되지 않은 상태입니다.", HttpStatus.UNAUTHORIZED); // 상태 코드 : 401
        }
    }

    // 카카오 로그인
    @PostMapping("/kakaoLogin")
    public ResponseEntity<String> kakaoLogin(@RequestBody Map<String, Object> kakaoData) throws JSONException {
        // @RequestBody : Http 요청 본문을 Map<String, Object> 형태로 파싱하여 kakaoData 라는 변수에 저장하고 있으며, 해당 어노테이션은 Http 요청 본문을 자바 객체로 변환할 때 사용한다.

        // kakaoData에서 "access_token" 이라는 key를 사용하여 카카오 토큰을 추출해서 변수에 저장
        // "access_token" 이라는 이름의 key는 OAuth2.0 인증 방식에서 사용하는 표준 키이다.
        // Outh 2.0 : 사용자 인증을 위한 일종의 표준 프로토콜로, 카카오, 구글 등 많은 서비스에서 이를 사용하고 있다.
        String kakaoToken = (String) kakaoData.get("access_token");

        // 만약 kakaoToken이 null, 혹은 비어있다면,
        if (kakaoToken == null || kakaoToken.isEmpty()) {
            return new ResponseEntity<>("Invalid or missing Kakao token", HttpStatus.BAD_REQUEST); // 코드 : 400 을 반환
        }

        // 카카오 사용자 정보 조회
        String kakaoUserInfo = requestKakaoUserInfo(kakaoToken);

        // JSON 파싱
        // 카카오 사용자 정보를 JSON 객체로 파싱하여 변수에 저장, 이를 통해 JSON 형식의 데이터를 쉽게 다룰 수 있게 된다.
        JSONObject jsonObject = new JSONObject(kakaoUserInfo);

        // 바로 위의 코드에서 생성한 변수에서 "id" 와 "nickname" 정보를 추출하여 각각 새로운 변수에 저장
        String kakaoId = jsonObject.get("id").toString();
        String kakaoNickName = jsonObject.getJSONObject("properties").get("nickname").toString(); // "properties" 와, "nickname" 라는 key의 이름들은 카카오에서 지정한 것

        // 회원 정보 조회 및 처리
        if (!memberService.kakaoSignUpCheck(kakaoNickName)) {
            // 카카오 닉네임이 가입되어 있지 않은 경우, 회원 가입 후 로그인 처리
            log.warn("카카오 닉네임 : " + kakaoNickName + "은 가입되어 있지 않습니다.");
            memberDto.setNickName(kakaoNickName);
            memberService.kakaoSignUp(memberDto);
        }

        // 카카오 닉네임이 이미 가입되어 있는 경우(또는 방금 가입한 경우), 로그인 처리
        String token = memberService.kakaoLogin(kakaoNickName);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    // 카카오 사용자 정보 조회
    private String requestKakaoUserInfo(String kakaoToken) {
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
}