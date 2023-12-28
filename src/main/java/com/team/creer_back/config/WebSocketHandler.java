package com.team.creer_back.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.creer_back.dto.chat.ChatMessageDto;
import com.team.creer_back.service.chat.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler { // afterConnectionEstablished : 웹 소켓 연결 성공 직후에 호출되는 메서드로 TextWebSocketHandler 클래스에 정의되어 있다.
    // JSON 문자열을 객체로, 객체를 JSON 문자열로 변환하기 위해 필요한 객체
    private final ObjectMapper objectMapper;

    // 이벤트 발행을 위한 객체
    private final ApplicationEventPublisher eventPublisher;

    private SessionService sessionService;

    @Autowired
    WebSocketHandler(ObjectMapper objectMapper, ApplicationEventPublisher eventPublisher, SessionService sessionService) {
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
        this.sessionService = sessionService;
    }

    // 클라이언트에서 사용자가 채팅방에 입장해서, 서버로 메시지를 전송할 때 발생하는 이벤트를 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 서버에서는 클라이언트가 전송한 메시지를 추출하고, JSON 형태의 문자열을 ChatMessageDto 객체로 변환
        String payload = message.getPayload(); // 페이로드 : 순수한 데이터를 의미
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

        // 세션에 발신자의 식별자를 저장
        if(chatMessage.getSender() != null) {
            session.getAttributes().put("memberEmail", chatMessage.getSender());
        }
        else{
            log.error("WebSocketHandler handleTextMessage chatMessage.getSender() 의 값이 NULL 입니다.!");
        }

        // 세션과 채팅방 ID를 매핑
        sessionService.putSession(session, chatMessage.getChatRoom());

        // 메시지 타입에 따라 해당하는 이벤트를 발행
        if (chatMessage.getType() == ChatMessageDto.MessageType.ENTER) { // 입장
            eventPublisher.publishEvent(new SessionEnteredEvent(session, chatMessage));
        } else if (chatMessage.getType() == ChatMessageDto.MessageType.CLOSE) { // 퇴장
            eventPublisher.publishEvent(new SessionExitedEvent(session, chatMessage));
        } else { // 일반
            eventPublisher.publishEvent(new MessageReceivedEvent(session, chatMessage));
        }
    }

    // 웹소켓 연결이 종료되면 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결이 종료된 세션을 Map에서 제거
        String roomId = sessionService.removeSession(session);
        if (roomId != null) {
            // 연결 종료 메시지 생성 후 이벤트 발행
            ChatMessageDto chatMessage = new ChatMessageDto();
            chatMessage.setType(ChatMessageDto.MessageType.CLOSE);
            chatMessage.setChatRoom(roomId);
            eventPublisher.publishEvent(new SessionDisconnectedEvent(session, chatMessage));
        }
    }

    // 이벤트를 처리하기 위한 이벤트 클래스들 정의
    public abstract class SessionEvent extends ApplicationEvent { // 내부 클라스
        // abstract : 추상 클래스로, 하위 클래스에 의해 확장되어야 하는 클래스
        private final WebSocketSession session;
        private final ChatMessageDto chatMessage;

        public SessionEvent(WebSocketSession session, ChatMessageDto chatMessage) {
            super(session);
            this.session = session;
            this.chatMessage = chatMessage;
        }

        public WebSocketSession getSession() {
            return this.session;
        }
        public ChatMessageDto getChatMessage() {
            return this.chatMessage;
        }
    }

    // 채팅방 입장 이벤트 클래스
    public class SessionEnteredEvent extends SessionEvent {
        public SessionEnteredEvent(WebSocketSession session, ChatMessageDto chatMessage) {
            super(session, chatMessage);
        }
    }

    // 채팅방 퇴장 이벤트 클래스
    public class SessionExitedEvent extends SessionEvent {
        public SessionExitedEvent(WebSocketSession session, ChatMessageDto chatMessage) {
            super(session, chatMessage);
        }
    }

    // 메시지 수신 이벤트 클래스
    public class MessageReceivedEvent extends SessionEvent {
        public MessageReceivedEvent(WebSocketSession session, ChatMessageDto chatMessage) {
            super(session, chatMessage);
        }
    }

    // 연결 종료 이벤트 클래스
    public class SessionDisconnectedEvent extends SessionEvent {
        public SessionDisconnectedEvent(WebSocketSession session, ChatMessageDto chatMessage) {
            super(session, chatMessage);
        }
    }
}

/*
채팅방에 사용자가 입장하면, 해당 사용자의 웹 소켓 세션과 채팅방의 아이디를 맵에 저장한다.
이렇게 하면 각 웹 소켓 세션이 어떤 채팅방에 연결되어 있는지 즉시 확인이 가능하다.
또한 웹 소켓 세션을 키로 사용해, 맵에서 채팅방의 아이디를 가져올 수 있는데,
이를 통해 특정 사용자가 보낸 메시지를 해당 사용자가 접속해있는 채팅방의 모든 사용자에게 전달이 가능하다.

채팅방과 채팅 메시지를 데이터 베이스에 저장하는 경우에도 맵이 필요한 이유 :
1. 실시간 성 : 데이터 베이스에 접근하여 정보를 가져오는 것은 시간이 소요된다.
반면, 메모리에 저장된 맵에서 데이터를 가져오는 것은 즉시 가능하다.
고로 실시간 채팅을 구현하기 위해서는 맵이 필요하다.

2. 세션 관리 : 맵은 채팅방에 접속한 사용자의 세션 정보를 저장 및 관리하는데 사용된다.
사용자가 채팅방에 입장 및 퇴장할 때, 해당 사용자의 세션 정보를 맵에서 추가하거나 제거한다.
이를 통해 사용자의 연결 상태를 실시간으로 파악 및 관리가 가능하다.
*/