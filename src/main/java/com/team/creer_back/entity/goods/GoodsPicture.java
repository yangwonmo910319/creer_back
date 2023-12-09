package com.team.creer_back.entity.goods;

import com.team.creer_back.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)  // 물품 번호로 리뷰 찾기
    @JoinColumn(name = "goods_Detail_id")
    private GoodsDetail goodsDetail;

    @Lob
    private String goodsPictures;          // 이미지 주소


}
