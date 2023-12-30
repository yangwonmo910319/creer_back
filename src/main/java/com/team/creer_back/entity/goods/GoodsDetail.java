package com.team.creer_back.entity.goods;

import com.team.creer_back.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "goodsDetail")
@Getter
@Setter
@ToString
@NoArgsConstructor
// 상품 상세페이지
public class GoodsDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsDetailId;
    private String goodsCategory;       // 카테고리
    @Lob
    private String goodsPic;            // 상품 사진
    @Column(length = 1000)
    private String goodsDesc;            // 상품 설명
    private Long goodsStock;         // 상품 재고
    private String goodsTitle;          // 상품 이름

    @ManyToOne(fetch = FetchType.LAZY)  // 판매자 닉네임, 프사
    @JoinColumn(name = "member_id")
    private Member member;

    private Long goodsPrice;          // 상품 가격
    private String goodsDeliveryFee;    // 배달비
    private String goodsStatus;    // 현재 판매 상태

    //게시글 삭제시 리뷰도 함께 삭제
    @OneToMany(mappedBy = "goodsDetail", cascade = CascadeType.REMOVE)
    private List<GoodsReview> reviews;

    //게시글 삭제시  사진도 함께 삭제
    @OneToMany(mappedBy = "goodsDetail", cascade = CascadeType.REMOVE)
    private List<GoodsPicture> pictures;

    //게시글 삭제시 옵션도 함께 삭제
    @OneToMany(mappedBy = "goodsDetail", cascade = CascadeType.REMOVE)
    private List<GoodsOption> options;

    // 하나의 상품에 대한 여러 구매 정보를 저장할 수 있도록 OneToMany 관계 설정
    @OneToMany(mappedBy = "goodsDetail", cascade = CascadeType.REMOVE)
    private List<GoodsPurchase> purchase;
}
