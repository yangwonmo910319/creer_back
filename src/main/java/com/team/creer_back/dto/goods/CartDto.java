package com.team.creer_back.dto.goods;

import com.team.creer_back.entity.member.Member;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Lob;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder // 모든 필드를 사용하여 빌더 패턴 생성 가능
public class CartDto {
    private Long cartId;  // PK
    private Member buyer;  //구매자 PK
    private Member seller; // 판매자 PK
    private Long goodsDetailId; //상품 PK
    private String option;//선택 옵션
    private Long quantity;//수량
    private String title;//제목
    @Lob
    private String goodsImg;//상품 이미지
    private Long price;//가격
}
