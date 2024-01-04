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
import com.team.creer_back.repository.goods.PurchaseRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
//@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository, MemberRepository memberRepository,
                        PurchaseRepository purchaseRepository, ModelMapper modelMapper) {
        this.goodsRepository = goodsRepository;
        this.memberRepository = memberRepository;
        this.purchaseRepository = purchaseRepository;
        this.modelMapper = modelMapper;
    }
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



    // 구매 목록이 있으면 에러 발생
//    public List<GoodsDetailDto> MyGoods() {
//        Long memberId = getCurrentMemberId();
//        List<GoodsDetail> goodsDetails = goodsRepository.findByMemberId(memberId);
//
//        return goodsDetails.stream()
//                .map(goodsDetail -> {
//                    GoodsDetailDto goodsDetailDto = modelMapper.map(goodsDetail, GoodsDetailDto.class);
//
//                    List<GoodsPurchaseDto> purchasesInfo = goodsDetail.getPurchase().stream()
//                            .map(goodsPurchase -> {
//                                GoodsPurchaseDto purchaseDto = modelMapper.map(goodsPurchase, GoodsPurchaseDto.class);
//                                purchaseDto.setGoodsDetailId(goodsDetailDto);
//                                return purchaseDto;
//                            })
//                            .collect(Collectors.toList());
//
//                    goodsDetailDto.setPurchase(purchasesInfo);
//                    return goodsDetailDto;
//                })
//                .collect(Collectors.toList());
//    }



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
    // 경매 목록 조회
    public List<GoodsDetailDto> getAuctionList( ) {
        List<GoodsDetail> goodsDetails = goodsRepository.findBygoodsStatusContaining("auction");
        List<GoodsDetailDto> goodsDetailDtos = new ArrayList<>();

        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetailDtos.add(goodsEntityToDto(goodsDetail));
        }
        return goodsDetailDtos;

    }
    @Transactional
    public Boolean updatePrice(int id,int price) {
        try{
            Long memberId = getCurrentMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            GoodsDetail goodsDetail = goodsRepository.findById((long) id).orElseThrow(
                    () -> new RuntimeException("상품이 없습니다"));

            if(price>goodsDetail.getGoodsPrice()){
                goodsDetail.setGoodsPrice((long) price);
                goodsDetail.setGoodsStatus("auction = " +member.getNickName());
                goodsRepository.save(goodsDetail);
                return true;
            }else {
                return false;
            }
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Transactional
    public Boolean updatePrice2(int id,int price) {
        try{
            Long memberId = getCurrentMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            GoodsDetail goodsDetail = goodsRepository.findById((long) id).orElseThrow(
                    () -> new RuntimeException("상품이 없습니다"));


                goodsDetail.setGoodsPrice(goodsDetail.getGoodsPrice()+ (long) price);
                goodsDetail.setGoodsStatus("auction = " +member.getNickName());
                goodsRepository.save(goodsDetail);
                return true;
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public GoodsDetailDto getGoods(Long goodsId) {
        GoodsDetail goodsDetail = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
        GoodsDetailDto goodsDetailDto = new GoodsDetailDto();
        goodsDetailDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());
        goodsDetailDto.setGoodsCategory(goodsDetail.getGoodsCategory());
        goodsDetailDto.setGoodsPic(goodsDetail.getGoodsPic());
        goodsDetailDto.setGoodsDesc(goodsDetail.getGoodsDesc());
        goodsDetailDto.setGoodsStock(goodsDetail.getGoodsStock());
        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());
        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());
        goodsDetailDto.setGoodsStatus(goodsDetail.getGoodsStatus());
        goodsDetailDto.setAuctionDate(goodsDetail.getAuctionDate());
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
    public Long insertGoods(GoodsDetailDto goodsDetailDto ,String  auctionTime) {


        try {
            GoodsDetail goodsDetail = new GoodsDetail();
            Long memberId = getCurrentMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            goodsDetail.setGoodsCategory(goodsDetailDto.getGoodsCategory());
            goodsDetail.setGoodsPic(goodsDetailDto.getGoodsPic());
            goodsDetail.setGoodsDesc(goodsDetailDto.getGoodsDesc());
            goodsDetail.setGoodsStock(goodsDetailDto.getGoodsStock());
            goodsDetail.setGoodsTitle(goodsDetailDto.getGoodsTitle());
            goodsDetail.setGoodsPrice(goodsDetailDto.getGoodsPrice());
            goodsDetail.setGoodsDeliveryFee(goodsDetailDto.getGoodsDeliveryFee());
            goodsDetail.setGoodsStatus(goodsDetailDto.getGoodsStatus());
            try {
                String isoDateTimeString = auctionTime; // 클라이언트에서 전송한 ISO 8601 형식의 문자열
                LocalDateTime dateTime = LocalDateTime.parse(isoDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
                goodsDetail.setAuctionDate(dateTime);
                goodsDetail.setGoodsStock(1L);
                // 변환된 날짜와 시간을 사용하는 코드
            } catch (DateTimeParseException e) {
                // 오류 처리
                e.printStackTrace();
            }
            goodsDetail.setMember(member);
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
            goodsDetail.setGoodsStock(goodsDetailDto.getGoodsStock());
            goodsDetail.setGoodsTitle(goodsDetailDto.getGoodsTitle());
            goodsDetail.setGoodsPrice(goodsDetailDto.getGoodsPrice());
            goodsDetail.setGoodsDeliveryFee(goodsDetailDto.getGoodsDeliveryFee());
            goodsDetail.setGoodsStatus(goodsDetailDto.getGoodsStatus());
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
        goodsDetailDto.setGoodsStock(goodsDetail.getGoodsStock());
        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());   // 상품 이름
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());   // 상품 가격
        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());// 배달비
        goodsDetailDto.setGoodsStatus(goodsDetail.getGoodsStatus());
        goodsDetailDto.setAuctionDate(goodsDetail.getAuctionDate());
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
        MemberDto memberDto = new MemberDto();
        goodsDetailDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());     //기본키
        goodsDetailDto.setGoodsCategory(goodsDetail.getGoodsCategory());//카테고리
        goodsDetailDto.setGoodsPic(goodsDetail.getGoodsPic());//상품 사진
        goodsDetailDto.setGoodsDesc(goodsDetail.getGoodsDesc());//상품 설명
        goodsDetailDto.setGoodsStock(goodsDetail.getGoodsStock());    // 상품 배송/환불/교환 안내
        goodsDetailDto.setGoodsTitle(goodsDetail.getGoodsTitle());   // 상품 이름
        goodsDetailDto.setGoodsPrice(goodsDetail.getGoodsPrice());   // 상품 가격
        goodsDetailDto.setGoodsDeliveryFee(goodsDetail.getGoodsDeliveryFee());// 배달비
        goodsDetailDto.setGoodsStatus(goodsDetail.getGoodsStatus());// 배달비
        memberDto.setNickName(goodsDetail.getMember().getNickName());
        List<GoodsPurchase> goodsPurchases = goodsDetail.getPurchase();
        List<GoodsPurchaseDto> goodsPurchaseDtos = new ArrayList<>();
        if (goodsPurchases != null && !goodsPurchases.isEmpty()) {
            for (GoodsPurchase purchase : goodsPurchases) {
                GoodsPurchaseDto purchaseDto = new GoodsPurchaseDto();
                purchaseDto.setId(purchase.getId());
                purchaseDto.setBuyer(memberDto);
                purchaseDto.setOption(purchase.getOption());
                purchaseDto.setQuantity(purchase.getQuantity());
                purchaseDto.setStatus(purchase.getStatus());
                purchaseDto.setRequirements(purchase.getRequirements());
                purchaseDto.setReceiveName(purchase.getReceiveName());
                purchaseDto.setReceiveAdd(purchase.getReceiveAdd());
                purchaseDto.setReceiveNumber(purchase.getReceiveNumber());
                goodsPurchaseDtos.add(purchaseDto);
            }
        }
        goodsDetailDto.setPurchase(goodsPurchaseDtos);
        return goodsDetailDto;
    }

    @Transactional
    // 상품 정보 저장
    public void saveGoods(GoodsDetail goodsDetail) {
        goodsRepository.save(goodsDetail);
    }
}
