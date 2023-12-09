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
import java.util.Optional;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReivewRepository reivewRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository ;
    // 리뷰 등록
    public boolean insertReview(GoodsReviewDto goodsDetailDto) {
        try {
            GoodsReview goodsReview = new GoodsReview();
            Long memberId = Long.valueOf(1);
//            Long memberId = getCurrentMemberId();
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

    // 리뷰 전체 조회
    public List<GoodsReviewDto> getReviewList(Long num) {
            try {
                GoodsDetail goodsDetail = goodsRepository.findById(num).orElseThrow(
                        () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
                );

                List<GoodsReview> Reviews = reivewRepository.findByGoodsDetail(goodsDetail);
                List<GoodsReviewDto> ReviewsDot = new ArrayList<>();
                for (GoodsReview Review : Reviews) {
                    ReviewsDot.add(ReviewEntityToDto(Review));
                }
                return ReviewsDot;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


    // 리뷰 삭제
    public boolean deleteReview(Long id) {
        try {
            reivewRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 리뷰 수정
    public boolean updateReview(Long num,GoodsReviewDto goodsDetailDto) {
        try {
            GoodsReviewDto goodsReviewDto = new GoodsReviewDto();
            GoodsReview goodsReview = reivewRepository.findById(num).orElseThrow(
                    () -> new RuntimeException("해당 리뷰가 존재하지 않습니다.")
            );
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



    //엔티티 -> Dto data교체
    private GoodsReviewDto ReviewEntityToDto(GoodsReview goodsReview) {
        GoodsReviewDto goodsReviewDto = new GoodsReviewDto();
        MemberDto memberDto = new MemberDto();
        goodsReviewDto.setGoodsReviewId(goodsReview.getGoodsReviewId());     //기본키
        goodsReviewDto.setGoodsDetailId(goodsReview.getGoodsReviewId());     //기본키
        Member member = goodsReview.getMember();
        memberDto.setName(member.getImage());//판매자 사진
        memberDto.setNickName(member.getNickName());//판매자 닉네임
        goodsReviewDto.setMemberDto(memberDto);
        goodsReviewDto.setReviewDate(goodsReview.getReviewDate());     //기본키
        goodsReviewDto.setReviewStar(goodsReview.getReviewStar());     //기본키
        goodsReviewDto.setReviewImg(goodsReview.getReviewImg());     //기본키
        goodsReviewDto.setReviewContent(goodsReview.getReviewContent());     //기본키





        return goodsReviewDto;
    }

}