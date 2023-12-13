package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsDetailDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {
    private final ReivewRepository reivewRepository;
    private final PictureRepository pictureRepository;
    private final GoodsRepository goodsRepository ;


    // 사진 등록
//    public boolean insertPicture(GoodsPictureDto goodsPictureDto) {
//        try {
//            GoodsDetail goodsDetail = goodsRepository.findById(  goodsPictureDto.getGoodsDetailId()).orElseThrow(
//                    () -> new RuntimeException("해당 글이 존재하지 않습니다.")
//            );
//            log.warn(goodsPictureDto.getGoodsPictures().get(0));
//            List<String> pictureList = goodsPictureDto.getGoodsPictures();
//            for (String img : pictureList) {
//                log.warn("goodsPictureDto.getGoodsPictures().get(0)goodsPictureDto.getGoodsPictures().get(0)");
//                GoodsPicture goodsPicture = new GoodsPicture();
//                goodsPicture.setGoodsDetail(goodsDetail);
//                goodsPicture.setGoodsPictures(img);
//                pictureRepository.save(goodsPicture);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    //사진 한장 삭제
    public boolean deletePicture(Long goodsPictureId) {
        try {
            pictureRepository.deleteById(goodsPictureId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //사진 출력+삭제

    public List<GoodsPictureDto> removeAndRetrievePictures(Long goodsDetailId) {
            try {
                GoodsDetail goodsDetail = goodsRepository.findById(goodsDetailId).orElseThrow(
                        () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
                );
                List<GoodsPicture> pictures = pictureRepository.findByGoodsDetail(goodsDetail);
                List<GoodsPictureDto> pictureDtos = new ArrayList<>();
                for ( GoodsPicture goodsPicture : pictures) {
                    pictureDtos.add(PictureEntityToDto(goodsPicture));
                }
                return pictureDtos;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

//   //사진 수정
//    public boolean updatePicture(Long goodsPictureId, GoodsPictureDto goodsPictureDto) {
//        try {
//            GoodsPicture existingPicture = pictureRepository.findById(goodsPictureId)
//                    .orElseThrow(() -> new RuntimeException("해당 사진이 존재하지 않습니다."));
//
//
//            existingPicture.setGoodsPictures(goodsPictureDto.getGoodsPictures());
//
//            pictureRepository.save(existingPicture);
//
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
private GoodsPictureDto PictureEntityToDto(GoodsPicture goodsPicture) {
    GoodsPictureDto goodsPictureDto = new GoodsPictureDto();
    GoodsDetail goodsDetail = goodsPicture.getGoodsDetail();
    goodsPictureDto.setGoodsPictureId(goodsPicture.getGoodsPictureId());
    goodsPictureDto.setGoodsDetailId(goodsDetail.getGoodsDetailId());
    goodsPictureDto.setGoodsPictures(goodsPicture.getGoodsPictures());


    return goodsPictureDto;
}



}