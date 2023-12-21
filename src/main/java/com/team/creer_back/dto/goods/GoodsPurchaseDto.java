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
    private MemberDto buyer;
    private Long seller;
    private Long goodsDetailId;
    private String option;
    private Long quantity;
    private String status;
}

