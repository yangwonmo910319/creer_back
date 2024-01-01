package com.team.creer_back.controller.jwt;

import com.team.creer_back.dto.jwt.TokenDto;
import com.team.creer_back.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/refresh")
@RequiredArgsConstructor
public class RefreshController {  // 프론트와 백 연결해서 받아옴, 프레젠테이션 레이어, 웹 요청과 응답을 처리함
    private final AuthService authService;

    // accessToken 재발급
    // refreshToken은 accessToken 재발급하기 위해 필요
    @PostMapping("/new")
    public ResponseEntity<TokenDto> newToken(@RequestBody String refreshToken) {
        log.info("AuthController newToken refreshToken : {}", refreshToken);
        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken));
    }
}