package com.team.creer_back.dto.goods;

import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.common.util.impl.Log;

import java.sql.Clob;

@Getter
@Setter
public class GoodsDetailDto {
    private Long goodsDetailId;
    private String goodsCategory;       // 카테고리
    private String goodsPic;            // 상품 사진
    private String goodsDesc;           // 상품 설명
    private String goodsRefund;         // 상품 배송/환불/교환 안내
    private String goodsTitle;          // 상품 이름
    private MemberDto memberDto;      // 판매자(유저) 닉네임 + 프로필사진
    private String goodsPrice;          // 상품 가격
    private String goodsDeliveryFee;    // 배달비

}
