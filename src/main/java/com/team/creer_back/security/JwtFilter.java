package com.team.creer_back.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// JWT 인증 정보를 검증하고, 이를 현재 스레드의 Security Context 에 저장하는 클래스
// Security Context : 스레드마다 별도로 존재
@Slf4j
@RequiredArgsConstructor
// OncePerRequestFilter : HTTP 요청 : 필터링 = 1 : 1 를 보장
public class JwtFilter  extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    
    // 요청 헤더에서 토큰을 추출하는 메서드
    private String resolveToken(HttpServletRequest request) {
        // 해당 헤더에서 토큰을 추출
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        
        // 토큰 앞에 지정된 문자열이 존재할 경우, 이를 제거한 후 토큰을 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        
        return null;
    }

    // 실제 필터링을 수행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 토큰을 추출
        String jwt = resolveToken(request); 

        // 토큰이 유효하다면, 권한 등의 사용자 정보가 담긴 인증 객체를 가져와 Security Context 에 저장
        // Security Context에 저장된 인증 정보는 이후 요청 처리 과정에서 사용 가능
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}