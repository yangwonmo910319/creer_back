package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.dto.member.MemberDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPicture;
import com.team.creer_back.entity.goods.GoodsReview;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.PictureRepository;
import com.team.creer_back.repository.goods.ReivewRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {
    private final ReivewRepository reivewRepository;
    private final PictureRepository pictureRepository;
    private final GoodsRepository goodsRepository ;
    // 사진 등록
    public boolean insertPicture(GoodsPictureDto goodsPictureDto) {
        try {
            GoodsDetail goodsDetail = goodsRepository.findById(    goodsPictureDto.getGoodsPictureId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다.")
            );
            List<String> pictureList = goodsPictureDto.getGoodsPictures();
            for (String img : pictureList) {
                GoodsPicture goodsPicture = new GoodsPicture();
                goodsPicture.setGoodsDetail(goodsDetail);
                goodsPicture.setGoodsPictures(img);
                pictureRepository.save(goodsPicture);
            }



            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }








}