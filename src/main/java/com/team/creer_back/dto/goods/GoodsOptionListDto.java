package com.team.creer_back.dto.goods;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoodsOptionListDto {
    private Long goodsOptionId;
    private List<Long> goodsDetailId;        // 상품번호
    private List<String> goodsOptionNum;       // 옵션번호
    private List<String> goodsOptionContent;   // 옵션내용


}
