package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsPurchaseDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPurchase;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.PurchaseRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    //구매 등록
    public Boolean insertPurchase(GoodsPurchaseDto goodsPurchaseDto) {
        try{
            GoodsPurchase goodsPurchase = new GoodsPurchase();
            Long memberId = getCurrentMemberId();//(로그인)구매자
            Member buyer = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다"));
            Member seller = memberRepository.findById(goodsPurchaseDto.getSeller()).orElseThrow(() -> new RuntimeException("판매자 아이디가 없습니다"));
            GoodsDetail goodsDetail = goodsRepository.findById(goodsPurchaseDto.getGoodsDetailId()).orElseThrow(() -> new RuntimeException("상품이 없습니다"));


            goodsPurchase.setBuyer(buyer);
            goodsPurchase.setSeller(seller);
            goodsPurchase.setGoodsDetail(goodsDetail);
            goodsPurchase.setOption(goodsPurchaseDto.getOption());
            goodsPurchase.setQuantity(goodsPurchaseDto.getQuantity());
            goodsPurchase.setStatus("결재 전");
            purchaseRepository.save(goodsPurchase);
            return true;
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
