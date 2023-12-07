package com.team.creer_back.entity.goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Entity
@Table(name = "payment")
@Getter @Setter
@ToString
@NoArgsConstructor
// 결제 상세 정보
public class Payment {
    @Id
    @GeneratedValue
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsOption")
    private GoodsOption goodsOption;
}
