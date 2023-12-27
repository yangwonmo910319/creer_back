package com.team.creer_back.entity.goods;

import com.team.creer_back.entity.member.Member;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "Cart")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue
    private Long cartId;

    // 구매자
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    // 판매자
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "goods_detail_id")
    private GoodsDetail goodsDetail;

    @Column(name = "cart_option")
    private String option;

    @Column(name = "cart_quantity")
    private Long quantity;

}
