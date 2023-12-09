package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;

    // 상품 전체 조회
    public List<GoodsDetailDto> getGoodsList() {
        List<GoodsDetail> goodsDetails = goodsRepository.findAll();
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;
    }

    // 상품 필터 조회
    public List<GoodsDetailDto> selctGoodsCategory(String keyword) {
        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsCategoryContaining(keyword);
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;

    }

    // 상품 제목 조회
    public List<GoodsDetailDto> selctGoodsTitle(String keyword) {
        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsTitleContaining(keyword);
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;

    }
    // 상품 삭제
    public boolean deleteGoods(Long id) {
        try {
            goodsRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // 상품 한개 조회
    public GoodsDetailDto selrctGoods(Long id) {
        GoodsDetail goodsDetail = goodsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 글이 존재하지 않습니다.")
        );
        return goodsEntityToDto(goodsDetail);
    }


    // 상품 등록
    public boolean insertGoods(GoodsDetailDto goodsDetailDto) {
        try {
            GoodsDetail goodsDetail = new GoodsDetail();
//            Long memberId = getCurrentMemberId();
            Long memberId = Long.valueOf(1);
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            goodsDetail.setGoodsDetailId(goodsDetailDto.getGoodsDetailId());
            goodsDetail.setGoodsCategory(goodsDetailDto.getGoodsCategory());
            goodsDetail.setGoodsPic(goodsDetailDto.getGoodsPic());
            goodsDetail.setGoodsDesc(goodsDetailDto.getGoodsDesc());
            goodsDetail.setGoodsRefund(goodsDetailDto.getGoodsRefund());
            goodsDetail.setGoodsTitle(goodsDetailDto.getGoodsTitle());
            goodsDetail.setGoodsPrice(goodsDetailDto.getGoodsPrice());
            goodsDetail.setGoodsDeliveryFee(goodsDetailDto.getGoodsDeliveryFee());
            goodsDetail.setMember(member);
            goodsRepository.save(goodsDetail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //상품 한개 수정
    public boolean updateGoods(Long id, GoodsDetailDto goodsDetailDto) {
        try {
            Long memberId = Long.valueOf(1);
//            Long memberId = getCurrentMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );

            GoodsDetail goodsDetail = goodsRepository.findById(goodsDetailDto.getGoodsDetailId()).orElseThrow(
                    () -> new RuntimeException("해당 상품이 존재하지 않습니다.")
            );

            goodsDetail.setGoodsCategory(goodsDetailDto.getGoodsCategory());
            goodsDetail.setGoodsPic(goodsDetailDto.getGoodsPic());
            goodsDetail.setGoodsDesc(goodsDetailDto.getGoodsDesc());
            goodsDetail.setGoodsRefund(goodsDetailDto.getGoodsRefund());
            goodsDetail.setGoodsTitle(goodsDetailDto.getGoodsTitle());
            goodsDetail.setGoodsPrice(goodsDetailDto.getGoodsPrice());
            goodsDetail.setGoodsDeliveryFee(goodsDetailDto.getGoodsDeliveryFee());
            goodsDetail.setMember(member);

            goodsRepository.save(goodsDetail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //엔티티 -> Dto data교체
    private GoodsDetailDto goodsEntityToDto(GoodsDetail goodsDetail) {
        GoodsDetailDto goodsDetailDto = new GoodsDetailDto();
        MemberDto memberDto = new MemberDto();
        goodsDetailDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());     //기본키
        goodsDetailDto.setGoodsCategory(goodsDetail.getGoodsCategory());//카테고리
        goodsDetailDto.setGoodsPic(goodsDetail.getGoodsPic());//상품 사진
        goodsDetailDto.setGoodsDesc(goodsDetail.getGoodsDesc());//상품 설명
        goodsDetailDto.setGoodsRefund(goodsDetail.getGoodsRefund());    // 상품 배송/환불/교환 안내
        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());   // 상품 이름
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());   // 상품 가격
        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());// 배달비
        Member member = goodsDetail.getMember();
        memberDto.setName(member.getImage());//판매자 사진
        memberDto.setNickName(member.getNickName());//판매자 닉네임
        goodsDetailDto.setMemberDto(memberDto);
        return goodsDetailDto;
    }

}
