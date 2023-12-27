package com.team.creer_back.utils;

import com.team.creer_back.dto.goods.CartDto;
import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.entity.goods.Cart;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    // ModelMapper : 자동으로 속성 이름이 같은 필드들을 매핑
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
