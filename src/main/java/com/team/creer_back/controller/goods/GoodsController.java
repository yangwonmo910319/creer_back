package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.service.goods.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService; // 생성자를 통해서 값을 참조할 수 있음

    // 굿즈 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<GoodsDetailDto>> goodsList() {
        List<GoodsDetailDto> list = goodsService.getGoodsList();
        return ResponseEntity.ok(list);
    }
}