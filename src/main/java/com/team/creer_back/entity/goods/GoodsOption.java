package com.team.creer_back.entity.goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "goodsOption")
@Getter
@Setter
@ToString
@NoArgsConstructor
// 디테일 옵션
public class GoodsOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsOptionId;
    private String goodsOptionNum;
    private String goodsOptionContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsDetail_id")
    private GoodsDetail goodsDetail;
}
