package com.team.creer_back.service.goods;


import com.team.creer_back.dto.goods.GoodsOptionDto;
import com.team.creer_back.dto.goods.GoodsOptionListDto;
import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsOption;
import com.team.creer_back.entity.goods.GoodsPicture;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.goods.OptionRepository;
import com.team.creer_back.repository.goods.PictureRepository;
import com.team.creer_back.repository.goods.ReivewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

    public class OptionService {

        private final GoodsRepository goodsRepository;
        private final OptionRepository optionRepository;

        // 옵션 등록 사진 등록
        public boolean insertOption(Long num, List<GoodsOptionDto> goodsOptionDtos) {
            try {
                GoodsDetail goodsDetail = goodsRepository.findById(num)
                        .orElseThrow(() -> new RuntimeException("해당 글이 존재하지 않습니다."));

                for (GoodsOptionDto goodsOptionDto : goodsOptionDtos) {
                    GoodsOption option = new GoodsOption();
                    option.setGoodsDetail(goodsDetail);
                    option.setGoodsOptionNum(goodsOptionDto.getGoodsOptionNum());
                    option.setGoodsOptionContent(goodsOptionDto.getGoodsOptionContent());
                    optionRepository.save(option);
                }

                return true; // 처리 완료 후 성공 반환
            } catch (Exception e) {
                e.printStackTrace();
                return false; // 예외 발생 시 실패 반환
            }
        }
    }