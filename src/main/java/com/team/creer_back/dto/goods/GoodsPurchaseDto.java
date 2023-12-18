package com.team.creer_back.dto.goods;


import com.team.creer_back.dto.member.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoodsPurchaseDto {

    private Long id;
    private MemberDto buyer;
    private Long seller;
    private Long goodsDetailId;
    private String option;
    private Long quantity;
    private String status;
}
