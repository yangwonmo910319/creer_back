package com.team.creer_back.dto.goods;

import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.member.Member;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder // 모든 필드를 사용하여 빌더 패턴 생성 가능
public class GoodsPurchaseDto {
    private Long id;
    private MemberDto buyer;  //구매자
    private GoodsDetailDto goodsDetailId;//상품PK
    private String option;          //선택한 옵션
    private Long quantity;        //구매 수량
    private String status;         //결재 상태
    private String requirements;         //요구 사항
    private String receiveName;         //받는 사람
    private String receiveAdd;       //받는 주소
    private String receiveNumber;          //받는 번호

}

