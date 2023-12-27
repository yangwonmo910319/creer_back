package com.team.creer_back.dto.goods;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder // 모든 필드를 사용하여 빌더 패턴 생성 가능
public class CartDto {
    private Long buyer;
    private Long seller;
    private GoodsDetailDto goodsDetail;
    private String option;
    private Long quantity;
}
