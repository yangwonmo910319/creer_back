package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;

    // 굿즈 전체 조회
    public List<GoodsDetailDto> getGoodsList() {
        List<GoodsDetail> goodsDetails = goodsRepository.findAll();
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for(GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(convertEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;
    }
    private GoodsDetailDto convertEntityToDto(GoodsDetail goodsDetail) {
        GoodsDetailDto goodsDetailDto = new GoodsDetailDto();
        goodsDetailDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());
        goodsDetailDto.setGoodsCategory(goodsDetail.getGoodsCategory());
        goodsDetailDto.setGoodsPic(goodsDetail.getGoodsPic());
        goodsDetailDto.setGoodsDesc(goodsDetail.getGoodsDesc());
        goodsDetailDto.setGoodsRefund(goodsDetail.getGoodsRefund());

        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());
        goodsDetailDto.setNickName(goodsDetail.getMember().getNickName());
        goodsDetailDto.setMemberImg(goodsDetail.getMember().getImage());
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());

        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());
        return goodsDetailDto;
    }

}
