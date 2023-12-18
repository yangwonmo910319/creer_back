package com.team.creer_back.entity.goods;

import com.team.creer_back.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "goods_purchase")
@Getter
@Setter
@ToString
@NoArgsConstructor
// 상품 구매
public class GoodsPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 구매자
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    // 판매자
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    // 상품
    @ManyToOne
    @JoinColumn(name = "goods_detail_id")
    private GoodsDetail goodsDetail;

    @Column(name = "Purchase_option")
    private String option;

    @Column(name = "Purchase_quantity")
    private Long quantity;

    @Column(name = "Purchase_status")
    private String status;
}