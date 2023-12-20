package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.CartDto;
import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.goods.GoodsPurchaseDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPurchase;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.PurchaseRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service

public class PurchaseService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseService(GoodsRepository goodsRepository, MemberRepository memberRepository, PurchaseRepository purchaseRepository) {

        this.goodsRepository = goodsRepository;
        this.memberRepository = memberRepository;
        this.purchaseRepository = purchaseRepository;
    }
    //구매 등록
    public Boolean insertPurchase(GoodsPurchaseDto goodsPurchaseDto) {
        try{
            GoodsPurchase goodsPurchase = new GoodsPurchase();
            Long memberId = getCurrentMemberId();//(로그인)구매자
            Member buyer = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다"));
            Member seller = memberRepository.findById(2L).orElseThrow(() -> new RuntimeException("판매자 아이디가 없습니다"));
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

   //구매 목록 출력
    public List<GoodsPurchaseDto> SelectPicture() {
        Long memberId = 2L;
        List<GoodsPurchase> goodsPurchases = purchaseRepository.findByBuyerId(memberId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));


        ModelMapper modelMapper = new ModelMapper();
        return goodsPurchases.stream()
                .map(goodsPurchase -> modelMapper.map(goodsPurchase, GoodsPurchaseDto.class))
                .collect(Collectors.toList());

    }




}
