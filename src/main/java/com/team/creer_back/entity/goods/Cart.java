package com.team.creer_back.entity.goods;

import com.team.creer_back.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Cart")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "goods_detail_id")
    private GoodsDetail goodsDetail;
}
