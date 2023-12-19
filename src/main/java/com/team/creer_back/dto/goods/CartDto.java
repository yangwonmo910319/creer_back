package com.team.creer_back.dto.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long memberId;
    private Long goodsDetailId;
}
