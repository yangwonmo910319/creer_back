package com.team.creer_back.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 접근 권한 없을 때 403 에러
@Component // Bean으로 등록
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override // 권한이 없는 경우 403 Forbidden 에러를 리턴할 클래스
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 필요한 권환이 없이 접근하려 할때 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN); // 권한이 없으면 403 Forbidden 에러를 리턴
    }
}