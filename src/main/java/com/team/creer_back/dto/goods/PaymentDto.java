package com.team.creer_back.dto.goods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private Long paymentId;
    private Long goodsDetailId;          // 아이템 번호
    private String goodsPic;
    private Long goodsOptionId;          // 옵션 번호
    private String optionContent;        // 옵션 내용
    private String goodsDeliveryFee;     // 배달 비

    private String goodsSelectNum;
    private String goodsOrder;
    private Integer goodsTotalNum;
    private String moneyPayment;
    private String moneyCash;
    private String moneyStatus;
    private String memberEmail;
}
