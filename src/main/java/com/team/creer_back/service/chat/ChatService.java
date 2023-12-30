package com.team.creer_back.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.creer_back.config.WebSocketHandler;
import com.team.creer_back.dto.chat.ChatMessageDto;
import com.team.creer_back.dto.chat.ChatRoomResDto;
import com.team.creer_back.entity.chat.ChatMessage;
import com.team.creer_back.entity.chat.ChatRoom;
import com.team.creer_back.entity.chat.ChatRoomMember;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.chat.ChatMessageRepository;
import com.team.creer_back.repository.chat.ChatRoomMemberRepository;
import com.team.creer_back.repository.chat.ChatRoomRepository;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Transactional(readOnly = true)
@Service // 채팅 관련 로직을 수행
public class ChatService {
    // ObjectMapper : 객체와 JSON 문자열을 서로 직렬화 및 역직렬화하기 위해 사용되는 클래스
    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final SessionService sessionService;
    private final GoodsRepository goodsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChatService(ObjectMapper objectMapper, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, MemberRepository memberRepository, ChatRoomMemberRepository chatRoomMemberRepository, SessionService sessionService, GoodsRepository goodsRepository, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.memberRepository = memberRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.sessionService = sessionService;
        this.goodsRepository = goodsRepository;
        this.modelMapper = modelMapper;
    }


    // [1] 채팅방 관리 메서드
    // [1-1] 모든 채팅방을 탐색
    public List<ChatRoomResDto> findAllRoom() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        return chatRooms.stream()
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomResDto.class))
                .collect(Collectors.toList());
    }

    // [1-2] 특정 ID를 가진 채팅방을 탐색
    public ChatRoomResDto findRoomById(Long roomId) throws Exception {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new Exception("ChatService findRoomById NULL 값이 들어왔습니다!"));
        return modelMapper.map(chatRoom, ChatRoomResDto.class);
    }

    // [1-3] 새로운 채팅방을 생성하고, 해당 채팅방의 정보를 반환
    @Transactional
    public ChatRoomResDto createRoom(Long goodsId) {
        Long buyerId = getCurrentMemberId();
        String chatRoomBuyerName = memberRepository.findById(buyerId)
                .map(member -> member.getName())
                .orElseThrow(() -> new EntityNotFoundException("ChatService createRoom buyerId에 NUll 값이 받아와졌습니다!"));

        String chatRoomSellerName = goodsRepository.findById(goodsId)
                .map(goodsDetail -> goodsDetail.getMember().getNickName())
                .orElseThrow(() -> new EntityNotFoundException("ChatService createRoom goodsId에 NUll 값이 받아와졌습니다!"));

        String chatRoomName = chatRoomSellerName + "님과 " + chatRoomBuyerName + "님의 채팅방";

        // 해당 이름의 채팅방이 이미 존재한다면,
        ChatRoom chatRoom = chatRoomRepository.findByName(chatRoomName);
        if(chatRoom == null) {
            chatRoom = ChatRoom.builder()
                    .name(chatRoomName)
                    .regDate(LocalDateTime.now())
                    .build();

            chatRoom = chatRoomRepository.save(chatRoom); // 수정과 같이 변경 감지가 아닌 경우에는 필수
        }

        return modelMapper.map(chatRoom, ChatRoomResDto.class);
    }


    // [1-4] 이전 채팅 로그를 호출
    public List<ChatMessageDto> getPreviousMessages(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_IdWithSender(roomId);

        return chatMessages.stream()
                .map(chatMessage -> modelMapper.map(chatMessage, ChatMessageDto.class))
                .collect(Collectors.toList());
    }

    // [1-5] 채팅방을 삭제
    @Transactional
    public void removeRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    // [2] 채팅 세션 관리 메서드
    // [2-1] 채팅방에 입장한 세션을 추가하고 입장 메시지를 전송
    @Transactional
    public void addSessionAndHandleEnter(Long roomId, WebSocketSession session, Long memberId, ChatMessageDto chatMessageDto) throws Exception {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new Exception("1. ChatService addSessionAndHandleEnter NULL 값이 들어왔습니다!"));
        ;
        if (chatRoom != null) {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new Exception("2. ChatService addSessionAndHandleEnter NULL 값이 들어왔습니다!"));
            if (member != null) {
                // 채팅방에 입장한 회원을 ChatRoomMember 에 추가하기 전에 중복 여부 확인
                ChatRoomMember existingMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member);
                if (existingMember == null) {
                    ChatRoomMember chatRoomMember = new ChatRoomMember();
                    chatRoomMember.setChatRoom(chatRoom);
                    chatRoomMember.setMember(member);

                    // MemberId와 사용자 이름을 세션의 속성으로 저장
                    session.getAttributes().put("memberId", member.getId());
                    session.getAttributes().put("memberName", member.getName()); // 새로운 라인 추가
                    log.warn("addSessionAndHandleEnter 로그 : " + member.getId());
                } else {
                    log.warn("addSessionAndHandleEnter: Member already in the room.");

                    // 중복된 회원이 채팅방에 이미 있으면 해당 정보로 sender 를 설정
                    chatMessageDto.setSender(existingMember.getMember().getName());
                }
            }
            if (chatMessageDto.getSender() != null) {
                chatMessageDto.setMessage(chatMessageDto.getSender() + "님이 입장했습니다.");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(ChatMessage.MessageType.ENTER);
                chatMessage.setMessage(chatMessageDto.getMessage());
                chatMessage.setChatRoom(chatRoom);
                chatMessage.setSender(memberRepository.findByName(chatMessageDto.getSender()).orElse(null));
            }
            log.debug("New session added: " + session);
        }
    }

    // [2-2] 채팅방에서 퇴장한 세션을 제거하고 퇴장 메시지를 전송
    @Transactional
    public void removeSessionAndHandleExit(Long roomId, WebSocketSession session, ChatMessageDto chatMessageDto) throws Exception {
        // ChatRoom room = findRoomById(roomId);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new Exception("ChatService removeSessionAndHandleExit에서 NULL 값이 들어왔습니다."));
        if (chatRoom != null) {
            // 세션에서 memberId를 가져옵니다.
            Long memberId = (Long) session.getAttributes().get("memberId");
            Member member = memberRepository.findById(memberId).orElse(null);

            if (member != null) {
                // ChatRoomMember에서 해당 Member를 찾아서 삭제
                ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member);
                if (chatRoomMember != null) {
                    chatRoomMemberRepository.delete(chatRoomMember);
                }

                chatMessageDto.setMessage(member.getName() + "님이 퇴장했습니다.");

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(ChatMessage.MessageType.CLOSE);
                chatMessage.setMessage(chatMessageDto.getMessage());
                chatMessage.setChatRoom(chatRoom);
                chatMessage.setSender(member);

                sendMessageToAll(roomId, chatMessageDto); // this 사용시 트랙잭션이 작동하지 않을 수도 있다.
                log.debug("Member removed: " + member.getName());
            } else {
                log.debug("Member not found for Member ID: " + memberId);
            }
        }
    }

    // [2-3] 웹소켓 세션에 메시지를 전송
    @Transactional
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            String messageStr = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(messageStr));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    // [2-4] 각각 다른 세션을 가지고 있는, 채팅방에 있는 모든 회원에게 메시지를 전송
    @Transactional
    public void sendMessageToAll(Long roomId, ChatMessageDto messageDto) throws Exception {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new Exception("ChatService sendMessageToAll에서 NULL 값이 들어왔습니다."));
        if (chatRoom != null) {
            // ChatRoomMember에서 해당 ChatRoom에 속한 모든 Member를 찾는다.
            List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByChatRoom(chatRoom);
            for (ChatRoomMember chatRoomMember : chatRoomMembers) {
                Member member = chatRoomMember.getMember();
                WebSocketSession session = sessionService.findSessionByMemberId(member.getId());
                if (session != null) {
                    sendMessage(session, messageDto);
                }
            }
        }
    }

    // [3] 이벤트 핸들러 관련 메서드
    // [3-1] 새로운 세션이 채팅방에 입장했을 때의 이벤트를 처리
    @Transactional
    @EventListener // 이벤트 처리
    @Async
    public void handleSessionEnteredEvent(WebSocketHandler.SessionEnteredEvent event) throws Exception {
        WebSocketSession session = event.getSession();
        ChatMessageDto chatMessageDto = event.getChatMessage();
        String roomId = chatMessageDto.getChatRoom(); // ChatRoom의 ID를 탐색
        String memberEmail = (String) session.getAttributes().get("memberEmail");

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );

        Long memberId = member.getId();
        log.warn("로그 찍어보기" + memberId + ", 룸 아이디는 " + roomId);
        addSessionAndHandleEnter(Long.valueOf(roomId), session, memberId, chatMessageDto);
    }

    // [3-2] 채팅 메시지가 수신되었을 때의 이벤트를 처리
    @Transactional
    @EventListener // MessageReceivedEvent 타입의 이벤트를 처리 가능, 이벤트는 타입을 통해 매핑된다.
    @Async // 이벤트 핸들러(Listener)가 이벤트를 처리하는 동안, 이벤트 발행자는 다른 작업을 계속 수행 가능하게 설정
    public void handleMessageReceivedEvent(WebSocketHandler.MessageReceivedEvent event) throws Exception {
        ChatMessageDto chatMessageDto = event.getChatMessage();
        String chatRoomId = chatMessageDto.getChatRoom();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.valueOf(chatMessageDto.getType().name()));
        chatMessage.setMessage(chatMessageDto.getMessage());
        chatMessage.setChatRoom(chatRoomRepository.findById(Long.valueOf(chatRoomId)).orElseThrow(() -> new Exception("ChatService handleMessageReceivedEvent 에서 NULL값이 들어왔습니다.")));

        String senderEmail = chatMessageDto.getSender();
        log.warn("handleMessageReceivedEvent senderName : " + senderEmail);
        Optional<Member> senderOpt = memberRepository.findByEmail(senderEmail);
        if (senderOpt.isPresent()) {
            Member sender = senderOpt.get();
            chatMessage.setSender(sender);
        } else {
            log.error("handleMessageReceivedEvent sender = null 에러 발생!");
        }
        sendMessageToAll(Long.valueOf(chatRoomId), chatMessageDto);
    }

    // [3-3] 세션이 채탕방에서 퇴장했을 때의 이벤트를 처리
    @Transactional
    @EventListener
    @Async
    public void handleSessionExitedEvent(WebSocketHandler.SessionExitedEvent event) throws Exception {
        WebSocketSession session = event.getSession();
        ChatMessageDto chatMessageDto = event.getChatMessage();
        String roomId = chatMessageDto.getChatRoom();

        removeSessionAndHandleExit(Long.valueOf(roomId), session, chatMessageDto);
    }

    // [3-3] 세션이 연결이 끊어졌을 때 이벤트를 처리
    @Transactional
    @EventListener
    @Async
    public void handleSessionDisconnectedEvent(WebSocketHandler.SessionDisconnectedEvent event) throws Exception {
        WebSocketSession session = event.getSession();
        ChatMessageDto chatMessageDto = event.getChatMessage();
        String roomId = chatMessageDto.getChatRoom();
        removeSessionAndHandleExit(Long.valueOf(roomId), session, chatMessageDto);
    }
}