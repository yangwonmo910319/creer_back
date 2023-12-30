package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsAutcionDto;
import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.goods.GoodsAution;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsReview;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.AuctionRepository;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.ReivewRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository;

    // 시간 등록
    public boolean insertAuction( Long num,LocalDateTime auctionTime) {
        try {
            GoodsAution goodsAution = new GoodsAution();
            GoodsDetail goodsDetail = goodsRepository.findById(num).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다.")
            );
            goodsAution.setGoodsDetail(goodsDetail);
            LocalDateTime auctionDate = auctionTime;
            goodsAution.setAuctionDate(auctionDate);
            auctionRepository.save(goodsAution);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}