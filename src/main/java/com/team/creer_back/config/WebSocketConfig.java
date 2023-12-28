package com.team.creer_back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// 웹 소켓 서버 설정을 위한 클래스
@Configuration // 설정
@EnableWebSocket // 웹소켓

/*
클라이언트가 웹소켓 서버의 엔드 포인트에 접근을 시도하는 순간, 웹소켓 연결이 시작되게 된다.
단방향 통신이던 Rest Api 와는 다르게 웹소켓 통신은 양방향이고, 한번 연결이 성공하면, 서버나 클라이언트 둘 중 하나가 연결을 종료할 때까지 연결이 유지된다.
*/

public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    @Autowired
    WebSocketConfig(WebSocketHandler webSocketHandler){
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    // 웹 소켓 통신을 구현하기 위해서는 특정 엔드포인트에 대한 핸들러가 필요하다.
    // 이 핸들러는 클라이언트로부터의 연결 요청을 받아들이고 메시지를 받고 보내는 등의 역할을 수행한다.
    // 아래 코드는 웹소켓 핸들러를 특정 엔드 포인트에 연결 후, 모든 도메인에서의 접속을 허용한다.
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");

    }
}

// 즉, 해당 클래스는 웹 소켓 서버를 설정하고,
// 클라이언트의 웹 소켓 연결을 처리할 핸들러를 등록하는 역할을 수행한다.
// 이를 통해 클라이언트와 서버 간의 실시간 양방향 통신이 가능하다.

/*
웹소켓에서 엔드 포인트는 일종의 라우터 역할을 수행한다.
웹소켓 연결 요청이 서버에 도달했을 때, 이 엔드포인트는 해당 요청을 적절한 웹소켓 핸들러로 라우팅하는 역할을 한다.
이렇게 웹소켓 핸들러가 연결 요청을 받게 되면, 이후의 웹소켓 통신을 해당 핸들러가 처리하게 된다.
*/