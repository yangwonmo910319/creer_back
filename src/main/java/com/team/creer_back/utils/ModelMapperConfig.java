package com.team.creer_back.utils;


import com.team.creer_back.dto.chat.ChatMessageDto;
import com.team.creer_back.entity.chat.ChatMessage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // ChatMessage 에서 ChatMessageDto 로의 명시적인 매핑 설정 추가
        modelMapper.typeMap(ChatMessage.class, ChatMessageDto.class)
                .addMapping(src -> src.getChatRoom().getId().toString(), ChatMessageDto::setChatRoom);

        return modelMapper;
    }
}