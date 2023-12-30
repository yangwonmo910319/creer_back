package com.team.creer_back.dto.goods;

import com.team.creer_back.dto.member.MemberDto;
import lombok.*;

import javax.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor  // 모든 필드 값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor   // 파라미터가 없는 디폴트 생성자를 생성
@Builder  // 클래스 레벨에 붙이거나 생성자에 붙여주면 파라미터를 활용하여 빌더 패턴을 자동으로 생성해줌
public class GoodsAutcionDto {
    private Long goodsTimeId;
    private Long goodsDetailId;       // FK-물품 번호 찾기
    private LocalDateTime auctionDate;       // 경매 시각


}