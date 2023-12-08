package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsReview;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.ReivewRepository;
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
public class ReviewService {
    private final ReivewRepository reivewRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository ;
//    // 굿즈 전체 조회
//    public List<GoodsDetailDto> getGoodsList() {
//        List<GoodsDetail> goodsDetails = goodsRepository.findAll();
//        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
//        for(GoodsDetail goodsDetail : goodsDetails) {
//            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
//        }
//        return goodsDetailDtos;
//    }
//
//    // 굿즈 필터 조회
//    public  List<GoodsDetailDto>  selctGoodsCategory(String keyword) {
//        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsCategoryContaining(keyword);
//        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
//        for(GoodsDetail goodsDetail : goodsDetails) {
//            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
//        }
//        return goodsDetailDtos;
//
//    }
//    // 굿즈 제목 조회
//    public  List<GoodsDetailDto>  selctGoodsTitle(String keyword) {
//        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsTitleContaining(keyword);
//        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
//        for(GoodsDetail goodsDetail : goodsDetails) {
//            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
//        }
//        return goodsDetailDtos;
//
//    }
//    // 굿즈 한개 조회
//    public GoodsDetailDto selrctGoods(Long id) {
//            GoodsDetail goodsDetail = goodsRepository.findById(id).orElseThrow(
//                    () -> new RuntimeException("해당 글이 존재하지 않습니다.")
//            );
//            return goodsEntityToDto(goodsDetail);
//        }



    // 리뷰 등록
    public boolean insertReview(GoodsReviewDto goodsDetailDto) {
        try {


            GoodsReview goodsReview = new GoodsReview();
            Long memberId = Long.valueOf(5);
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            GoodsDetail goodsDetail = goodsRepository.findById(goodsDetailDto.getGoodsDetailId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다.")
            );
            goodsReview.setGoodsDetail(goodsDetail);
            goodsReview.setMember(member);
            goodsReview.setReviewDate( goodsDetailDto .getReviewDate());
            goodsReview.setReviewStar(goodsDetailDto.getReviewStar());
            goodsReview.setReviewImg(goodsDetailDto.getReviewImg());
            goodsReview.setReviewContent(goodsDetailDto.getReviewContent());

            reivewRepository.save(goodsReview);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//
////굿즈 한개 수정
//public boolean updateGoods(Long id,GoodsDetailDto goodsDetailDto) {
//    try {
//        Long memberId = getCurrentMemberId();
//        Member member = memberRepository.findById(memberId).orElseThrow(
//                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
//        );
//
//        GoodsDetail goodsDetail = goodsRepository.findById(goodsDetailDto.getGoodsDetailId()).orElseThrow(
//                () -> new RuntimeException("해당 상품이 존재하지 않습니다.")
//        );
//
//        goodsDetail.setGoodsCategory(goodsDetailDto.getGoodsCategory());
//        goodsDetail.setGoodsPic(goodsDetailDto.getGoodsPic());
//        goodsDetail.setGoodsDesc(goodsDetailDto.getGoodsDesc());
//        goodsDetail.setGoodsRefund(goodsDetailDto.getGoodsRefund());
//        goodsDetail.setGoodsTitle(goodsDetailDto.getGoodsTitle());
//        goodsDetail.setGoodsPrice(goodsDetailDto.getGoodsPrice());
//        goodsDetail.setGoodsDeliveryFee(goodsDetailDto.getGoodsDeliveryFee());
//        goodsDetail.setMember(member);
//
//        goodsRepository.save(goodsDetail);
//        return true;
//    } catch (Exception e) {
//        e.printStackTrace();
//        return false;
//    }
//}
//
//


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