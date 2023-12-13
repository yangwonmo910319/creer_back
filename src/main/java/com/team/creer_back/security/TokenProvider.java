package com.team.creer_back.security;

import com.team.creer_back.dto.jwt.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// 추가된 라이브러리를 사용해서 JWT를 생성하고 검증하는 클래스

// 유저 정보로 jwt access/refresh 토큰 생성 및 재발급 + 토큰으로부터 유저 정보 받아옴
@Slf4j
@Component // Bean Configuration 파일에 Bean을 따로 등록 하지 않아도 사용할 수 있음
// 토큰 생성, 토큰 검증, 토큰에서 회원 정보 추출
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth"; // 토큰에 저장 되는 권한 정보의 key
    private static final String BEARER_TYPE = "Bearer"; // 토큰의 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 3; // 3분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7L * 24 * 60 * 60 * 1000; // 7일
    private final Key key; // 토큰을 서명 하기 위한 Key

    // 주의점 : 여기서 @Value는 'springframework.beans.factory.annotation.Value' 소속이다. lambok의 @Value와 착각하지 않기!
    public TokenProvider(@Value("${jwt.secret}") String secretKey) { //jwt.secret 값을 가져와서 JWT를 만들 때 사용하는 암호화 키값을 생성
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // HS512 알고리즘을 사용하는 키 생성
    }

    // 토큰 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한 정보 문자열 생성
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // 현재 시간
        // 재발행 할지 안 할지 보고 결정 해야 함
        // 토큰 만료 시간 설정
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // Access Token 생성
        // claim : payload 부분에는 토큰에 담을 정보가 들어있는데 여기에 담는 정보 한 조각을 의미(name,value의 한 쌍)
        // claim 종류 세가지 : registered(등록된 클레임), public(공개 클레임), private(비공개 클레임)
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // payload "sub": "name" 토큰 제목
                .claim(AUTHORITIES_KEY, authorities)  // payload "auth": "ROLE_USER" 권한자
                .setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022 (예시) 토큰의 만료시간
                .signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512" 알고리즘 해싱
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName()) // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)  // payload "auth": "ROLE_USER"
                .setExpiration(refreshTokenExpiresIn) // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
                .compact();

        // 토큰 정보를 담은 TokenDto 객체 생성
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화(암호화된 것을 알아 보기 쉽게 푸는 것)
        Claims claims = parseClaims(accessToken);

        // 토큰 복호화에 실패하면
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 토큰에 담긴 권한 정보들을 가져옴(클레임에서 권한 정보 가져오기)
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한 정보들을 이용해 유저 객체를 만들어서 반환, 여기서 User 객체는 UserDetails 인터페이스를 구현한 객체
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        // 유저 객체, 토큰, 권한 정보들을 이용해 인증 객체를 생성해서 반환
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    // 토큰의 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다."); // MalformedJwtException : 잘못된 JWT 서명
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다."); // ExpiredJwtException : 만료된 JWT 토큰
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다."); // UnsupportedJwtException : 지원되지 않는 JWT 토큰
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다."); // IllegalArgumentException : JWT 토큰이 잘못됨
        }
        return false;
    }
    // 토큰 복호화
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) { // ExpiredJwtException : 만료된 JWT 토큰
            return e.getClaims();
        }
    }
    // access 토큰 재발급
    public String generateAccessToken(Authentication authentication) {
        return generateTokenDto(authentication).getAccessToken();
    }
}