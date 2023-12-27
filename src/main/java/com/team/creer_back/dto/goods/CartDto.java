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
    private Long cartId;   // PK
    private Long buyer;   //구매자
    private Long seller;  //판매자
    private GoodsDetailDto goodsDetail;  //상품 PK
    private String option; //상품 옵션
    private Long quantity;//수량

}
