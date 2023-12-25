package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsPurchaseDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPurchase;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.PurchaseRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PurchaseService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseService(GoodsRepository goodsRepository, MemberRepository memberRepository, PurchaseRepository purchaseRepository, ModelMapper modelMapper) {
        this.goodsRepository = goodsRepository;
        this.memberRepository = memberRepository;
        this.purchaseRepository = purchaseRepository;
        this.modelMapper = modelMapper;
    }

    //구매 등록
    @Transactional
    public Boolean insertPurchase(GoodsPurchaseDto goodsPurchaseDto) {
        try{
            GoodsPurchase goodsPurchase = new GoodsPurchase();
            Long buyerId = getCurrentMemberId(); // 구매자
            Member buyer = memberRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다"));
            log.warn("혹시? : " + goodsPurchaseDto.getSeller());
            Member seller = memberRepository.findById(goodsPurchaseDto.getSeller()).orElseThrow(() -> new RuntimeException("판매자 아이디가 없습니다"));
            GoodsDetail goodsDetail = goodsRepository.findById(goodsPurchaseDto.getGoodsDetailId()).orElseThrow(() -> new RuntimeException("상품이 없습니다"));

            goodsPurchase.setBuyer(buyer);
            goodsPurchase.setSeller(seller);
            goodsPurchase.setGoodsDetail(goodsDetail);
            goodsPurchase.setOption(goodsPurchaseDto.getOption());
            goodsPurchase.setQuantity(goodsPurchaseDto.getQuantity());
            goodsPurchase.setStatus("결제 전");
            purchaseRepository.save(goodsPurchase);
               //재고 - 판매량 == 0 이면 판매중 -> 판매 정지로 변경
            if(goodsDetail.getGoodsStock()-goodsPurchaseDto.getQuantity()==0){
                goodsDetail.setGoodsStatus("stop");
                goodsRepository.save(goodsDetail);
            }
            //재고 - 판매량
            goodsDetail.setGoodsStock(goodsDetail.getGoodsStock() - goodsPurchaseDto.getQuantity());
            goodsRepository.save(goodsDetail);
            return true;
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   //구매 목록 출력
    public List<GoodsPurchaseDto> SelectPicture() {
        Long memberId = getCurrentMemberId(); // 구매자
        List<GoodsPurchase> goodsPurchases = purchaseRepository.findByBuyerId(memberId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
        return goodsPurchases.stream()
                .map(goodsPurchase -> modelMapper.map(goodsPurchase, GoodsPurchaseDto.class))
                .collect(Collectors.toList());
    }
}
