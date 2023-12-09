package com.team.creer_back.entity.goods;

import com.team.creer_back.entity.member.Member;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "goodsDetail")
@Getter @Setter
@ToString
@NoArgsConstructor
// 상품 상세페이지
public class GoodsDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsDetailId;
    private String goodsCategory;       // 카테고리
    private String goodsPic;            // 상품 사진
    @Column(length = 1000)
    private String goodsDesc;            // 상품 설명
    private String goodsRefund;         // 상품 배송/환불/교환 안내
    private String goodsTitle;          // 상품 이름

    @ManyToOne(fetch = FetchType.LAZY)  // 판매자 닉네임, 프사
    @JoinColumn(name = "member_id")
    private Member member;

    private String goodsPrice;          // 상품 가격
    private String goodsDeliveryFee;    // 배달비

    
    //게실글 삭제시 리뷰도 함께 삭제
    @OneToMany(mappedBy = "goodsDetail", cascade = CascadeType.REMOVE)
    private List<GoodsReview> reviews;
}
