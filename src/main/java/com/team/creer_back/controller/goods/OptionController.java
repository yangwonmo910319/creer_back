package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsOptionDto;
import com.team.creer_back.dto.goods.GoodsOptionListDto;
import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.entity.goods.GoodsOption;
import com.team.creer_back.service.goods.OptionService;
import com.team.creer_back.service.goods.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/option")
@RequiredArgsConstructor
public  class OptionController {
    private final OptionService optionService; // 생성자를 통해서 값을 참조할 수 있음

            //옵션 등록
            @PostMapping("/new/{num}")
            public ResponseEntity<Boolean> insertOption(@PathVariable Long num, @RequestBody List<GoodsOptionDto> goodsOptionDto) {
                boolean  list = optionService.insertOption(num,goodsOptionDto);

                return ResponseEntity.ok(list);
        }

}












