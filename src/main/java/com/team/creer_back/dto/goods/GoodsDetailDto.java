package com.team.creer_back.dto.goods;

import com.team.creer_back.dto.member.MemberDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class GoodsDetailDto {
    private Long goodsDetailId;
    private String goodsCategory;       // 카테고리

    @Lob
    private String goodsPic;            // 상품 사진
    @Lob
    private String goodsDesc;           // 상품 설명
    private Long goodsStock;         // 상품 재고
    private String goodsTitle;          // 상품 이름
    private MemberDto memberDto;      // 판매자(유저) 닉네임 + 프로필사진
    private Long goodsPrice;          // 상품 가격
    private String goodsDeliveryFee;    // 배달비
    private String goodsStatus;    // 현재 판매 상태
    private LocalDateTime auctionDate;   // 경매 날,시간
    private List<GoodsReviewDto> reviews;  // 모든 리뷰
    private List<GoodsOptionDto> options; // 모든 옵션
    private List<GoodsPurchaseDto> purchase; // 모든 구매 목록
}