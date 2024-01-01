package com.team.creer_back.controller.chat;

import com.team.creer_back.dto.chat.ChatMessageDto;
import com.team.creer_back.dto.chat.ChatRoomReqDto;
import com.team.creer_back.dto.chat.ChatRoomResDto;
import com.team.creer_back.service.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 채팅방 생성
    @PostMapping("/new")
    public ResponseEntity<String> createRoom(@Valid @RequestBody ChatRoomReqDto chatRoomReqDto) {
        ChatRoomResDto chatRoomResDto = chatService.createRoom(chatRoomReqDto.getGoodsId());
        return ResponseEntity.ok(chatRoomResDto.getId()); // ok : 정적 메서드
    }

    // 모든 채팅방의 목록 반환
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResDto>> findAllRoom() {
        return ResponseEntity.ok(chatService.findAllRoom());
    }

    // 방 정보 가져오기
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResDto> findRoomById(@PathVariable Long roomId) throws Exception {
        ChatRoomResDto room = chatService.findRoomById(roomId);

        if (room != null) {
            log.warn("findRoomById : " + room);
            return ResponseEntity.ok(room);
        } else {
            log.warn("findRoomById Error Null : " + room);
            return ResponseEntity.notFound().build();
        }
    }

    // 채팅방 삭제
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<String> removeRoom(@PathVariable Long roomId) {
        chatService.removeRoom(roomId);
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }

    // 이전 채팅 로그 불러오기
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getPreviousMessages(@PathVariable Long roomId) {
        List<ChatMessageDto> messages = chatService.getPreviousMessages(roomId);
        return ResponseEntity.ok(messages);
    }
}

// Q. List <> 안의 형식을 엔티티로 했을 때와 Dto로 했을 때의 차이?
// 엔티티는 데이터 베이스와 직접적으로 매핑되는 객체이기 때문에,
// 데이터 베이스의 구조가 변경되면 엔티티도 함께 변경되어야 하지만,
// Dto는 사용자에게 보여주는 화면에 맞추어 설계되기 때문에, 변경되어도 데이터 베이스에 영향을 주지 않는다.
// 엔티티와 달리 Dto를 사용하면 필요한 데이터만 선택적으로 전달해 데이터 보안을 강화할 수 있다.
// 또한 엔티티는 사용하지 않는 필드를 가지고 있을 수도 있기 때문에,
// Dto를 사용하여 필요한 필드만을 클라이언트에게 전달한다면 성능을 향상시킬 수 있다.
