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
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Cart 에 대해서TypeMap 설정 추가, 다른 Entity 및 DTO에는 영향 X
        modelMapper.createTypeMap(Cart.class, CartDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getBuyer().getId(), CartDto::setBuyer);
            mapper.map(src -> src.getSeller().getId(), CartDto::setSeller);
            mapper.map(src -> src.getGoodsDetail().getGoodsDetailId(), CartDto::setGoodsDetail);
            mapper.map(src -> modelMapper.map(src.getGoodsDetail(), GoodsDetailDto.class), CartDto::setGoodsDetail);
        });
        return modelMapper;
    }
}
