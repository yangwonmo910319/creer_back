package com.team.creer_back.entity.goods;


import com.team.creer_back.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "goods_auction")
@Getter @Setter
@ToString
@NoArgsConstructor
// 후기
public class GoodsAution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsTimeId;

    @ManyToOne(fetch = FetchType.LAZY)  // 물품 번호로 리뷰 찾기
    @JoinColumn(name = "goods_Detail_id")
    private GoodsDetail goodsDetail;

   private LocalDateTime auctionDate;   // 날짜 넣어줌

}
