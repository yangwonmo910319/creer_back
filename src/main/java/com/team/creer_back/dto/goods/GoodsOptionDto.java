package com.team.creer_back.dto.goods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsOptionDto {
    private Long goodsOptionId;
    private String goodsDetailId;
    private String goodsOptionNum;       // 옵션번호
    private String goodsOptionContent;   // 옵션내용

    private String goodsSelectNum;       // 수량 선택
    private String goodsOrder;           // 요청사항
    private String goodsTotalPrice;      // 총 금액 (배달비 제외)
}
