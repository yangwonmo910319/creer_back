package com.team.creer_back.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ChatRoomReqDto {
    @NotNull(message = "ChatRoomDto goodsId NULL 값이 들어왔습니다!")
    private Long goodsId;
}
