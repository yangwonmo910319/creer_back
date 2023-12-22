package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.goods.GoodsOptionDto;
import com.team.creer_back.dto.goods.GoodsPurchaseDto;
import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsOption;
import com.team.creer_back.entity.goods.GoodsPurchase;
import com.team.creer_back.entity.goods.GoodsReview;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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


    // 내 상품 전체 조회
    public List<GoodsDetailDto> getMyGoods() {
        Long memberId = getCurrentMemberId();
        List<GoodsDetail> goodsDetails = goodsRepository.findByMemberId(memberId);
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto2(goodsDetail));
        }
        return goodsDetailDtos;
    }


    // 상품 필터 조회
    public List<GoodsDetailDto> tagGoods(String keyword) {
        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsCategoryContaining(keyword);
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;

    }

    // 상품 제목 조회
    public List<GoodsDetailDto> TitleGoods(String keyword) {
        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsTitleContaining(keyword);
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;

    }

    // 상품 삭제
    @Transactional
    public boolean deleteGoods(Long id) {
        try {
            goodsRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    // 상품 한개 조회
//    public GoodsDetailDto getGoods(Long id) {
//        GoodsDetail goodsDetail = goodsRepository.findById(id).orElseThrow(
//                () -> new RuntimeException("해당 글이 존재하지 않습니다.")
//        );
//
//        return goodsEntityToDto(goodsDetail);
//    }

    public GoodsDetailDto getGoods(Long goodsId) {
        GoodsDetail goodsDetail = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
        GoodsDetailDto goodsDetailDto = new GoodsDetailDto();
        goodsDetailDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());
        goodsDetailDto.setGoodsCategory(goodsDetail.getGoodsCategory());
        goodsDetailDto.setGoodsPic(goodsDetail.getGoodsPic());
        goodsDetailDto.setGoodsDesc(goodsDetail.getGoodsDesc());
        goodsDetailDto.setGoodsRefund(goodsDetail.getGoodsRefund());
        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());
        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());
        //작성자(판매자) 정보
        Member member = goodsDetail.getMember();
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setImage(member.getImage());
        memberDto.setNickName(member.getNickName());
        goodsDetailDto.setMemberDto(memberDto);

        List<GoodsReview> reviews = goodsDetail.getReviews();
        List<GoodsReviewDto> reviewDtos = new ArrayList<>();
        List<GoodsOption> options = goodsDetail.getOptions();
        List<GoodsOptionDto> goodsOptionDtos = new ArrayList<>();
        for (GoodsReview review : reviews) {
            GoodsReviewDto reviewDto = new GoodsReviewDto();
            reviewDto.setGoodsReviewId(review.getGoodsReviewId());
            reviewDto.setGoodsDetailId(review.getGoodsDetail().getGoodsDetailId());
            // Member 정보를 MemberDto로 변환하여 할당
            Member member1 = review.getMember();
            if (member != null) {
                MemberDto memberDto1 = member1.toDto();
                reviewDto.setMemberDto(memberDto1);
            }
            reviewDto.setReviewContent(review.getReviewContent());
            reviewDto.setReviewDate(review.getReviewDate());
            reviewDto.setReviewImg(review.getReviewImg());
            reviewDto.setReviewStar(review.getReviewStar());
            // 다른 필요한 리뷰 정보 추가
            reviewDtos.add(reviewDto);        }
        for (GoodsOption option : options) {
            GoodsOptionDto goodsReviewDto = new GoodsOptionDto();
            goodsReviewDto.setGoodsOptionId(option.getGoodsOptionId());
            goodsReviewDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());
            goodsReviewDto.setGoodsOptionNum(option.getGoodsOptionNum());
            goodsReviewDto.setGoodsOptionContent(option.getGoodsOptionContent());
            // 다른 필요한 리뷰 정보 추가
            goodsOptionDtos.add(goodsReviewDto);
        }

        goodsDetailDto.setReviews(reviewDtos);
        goodsDetailDto.setOptions(goodsOptionDtos);
        return goodsDetailDto;
    }


    // 상품 등록
    @Transactional
    public Long insertGoods(GoodsDetailDto goodsDetailDto) {
        try {
            GoodsDetail goodsDetail = new GoodsDetail();
            Long memberId = getCurrentMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );

//            goodsDetail.setGoodsDetailId(goodsDetailDto.getGoodsDetailId());
            goodsDetail.setGoodsCategory(goodsDetailDto.getGoodsCategory());
            goodsDetail.setGoodsPic(goodsDetailDto.getGoodsPic());
            goodsDetail.setGoodsDesc(goodsDetailDto.getGoodsDesc());
            goodsDetail.setGoodsRefund(goodsDetailDto.getGoodsRefund());
            goodsDetail.setGoodsTitle(goodsDetailDto.getGoodsTitle());
            goodsDetail.setGoodsPrice(goodsDetailDto.getGoodsPrice());
            goodsDetail.setGoodsDeliveryFee(goodsDetailDto.getGoodsDeliveryFee());
            goodsDetail.setMember(member);
//            goodsRepository.save(goodsDetail);
            GoodsDetail savedGoodsDetail = goodsRepository.save(goodsDetail);

            return savedGoodsDetail.getGoodsDetailId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    //상품 한개 수정
    @Transactional
    public boolean updateGoods(Long id, GoodsDetailDto goodsDetailDto) {
        try {
            Long memberId = getCurrentMemberId();
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
        memberDto.setImage(member.getImage());//판매자 사진
        memberDto.setNickName(member.getNickName());//판매자 닉네임
        goodsDetailDto.setMemberDto(memberDto);
        return goodsDetailDto;
    }

    // 상품 메인 사진 등록
    public boolean insertPicture(GoodsDetailDto goodsDetailDto) {
        try {
            GoodsDetail goodsDetail = goodsRepository.findById(goodsDetailDto.getGoodsDetailId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다.")
            );
            goodsDetail.setGoodsPic(goodsDetailDto.getGoodsPic());
            goodsRepository.save(goodsDetail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 페이지네이션
    public List<GoodsDetailDto> getMovieList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<GoodsDetail> goodsDetails = goodsRepository.findAll(pageable).getContent();
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;
    }

    // 페이지 수 조회
    public int getMoviePage(Pageable pageable) {
        return goodsRepository.findAll(pageable).getTotalPages();
    }

    // DTO 변환
    private GoodsDetailDto goodsEntityToDto2(GoodsDetail goodsDetail) {
        GoodsDetailDto goodsDetailDto = new GoodsDetailDto();
        goodsDetailDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());     //기본키
        goodsDetailDto.setGoodsCategory(goodsDetail.getGoodsCategory());//카테고리
        goodsDetailDto.setGoodsPic(goodsDetail.getGoodsPic());//상품 사진
        goodsDetailDto.setGoodsDesc(goodsDetail.getGoodsDesc());//상품 설명
        goodsDetailDto.setGoodsRefund(goodsDetail.getGoodsRefund());    // 상품 배송/환불/교환 안내
        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());   // 상품 이름
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());   // 상품 가격
        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());// 배달비
        List<GoodsPurchase> goodsPurchases = goodsDetail.getPurchase();
        List<GoodsPurchaseDto> goodsPurchaseDtos = new ArrayList<>();
        for (GoodsPurchase goodsPurchase : goodsPurchases) {
            GoodsPurchaseDto goodsPurchaseDto = new GoodsPurchaseDto();

            Member member = goodsPurchase.getBuyer();
            if (member != null) {
                MemberDto memberDto = member.toDto();
                goodsPurchaseDto.setBuyer(memberDto);

            }


            goodsPurchaseDto.setId(goodsPurchase.getId());
            goodsPurchaseDto.setGoodsDetailId(goodsPurchase.getId());
            goodsPurchaseDto.setOption(goodsPurchase.getOption());
            goodsPurchaseDto.setQuantity(goodsPurchase.getQuantity());
            goodsPurchaseDto.setStatus(goodsPurchase.getStatus());

            // 다른 필요한 리뷰 정보 추가
            goodsPurchaseDtos.add(goodsPurchaseDto);
        }
        goodsDetailDto.setPurchase(goodsPurchaseDtos);
        return goodsDetailDto;
    }


    // 상품 정보 저장
    public void saveGoods(GoodsDetail goodsDetail) {
        goodsRepository.save(goodsDetail);
    }
}
