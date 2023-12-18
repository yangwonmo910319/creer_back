package com.team.creer_back.dto.goods;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoodsOptionDto {
    private Long goodsOptionId;
    private String goodsOptionNum;       // 옵션번호
    private String goodsOptionContent;   // 옵션내용
    private Long goodsDetailId;        // 상품번호
    private List<GoodsOptionDto> goodsOptionList;
}
