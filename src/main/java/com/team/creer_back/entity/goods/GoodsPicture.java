package com.team.creer_back.entity.goods;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "goods_picture")
@Getter @Setter
@ToString
@NoArgsConstructor
// 상품 사진
public class GoodsPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsPictureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_detail_id")
    private GoodsDetail goodsDetail;

    @Lob
    private String goodsPictures;          // 이미지 주소


}
