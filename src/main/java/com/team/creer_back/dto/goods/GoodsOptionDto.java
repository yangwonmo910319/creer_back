package com.team.creer_back.dto.goods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsOptionDto {
    private Long goodsOptionId;
    private Long goodsDetailId;        // 상품번호
    private String goodsOptionNum;       // 옵션번호
    private String goodsOptionContent;   // 옵션내용


}
