package com.team.creer_back.entity.goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "goodsOption")
@Getter @Setter
@ToString
@NoArgsConstructor
// 디테일 옵션
public class GoodsOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsOptionId;
    private String goodsOptionNum;
    private String goodsOptionContent;
    private String goodsSelectNum;       // 수량 선택
    private String goodsOrder;           // 요청사항
    private String goodsTotalPrice;     // 총 금액 (배달비 제외)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsDetail_id")
    private GoodsDetail goodsDetail;
}
