package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.service.goods.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService; // 생성자를 통해서 값을 참조할 수 있음


    // 굿즈 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<GoodsDetailDto>> selctGoodsList() {
        List<GoodsDetailDto> list = goodsService.getGoodsList();
        return ResponseEntity.ok(list);
    }
    // 굿즈 하나 조회
    @GetMapping("/listDetail/{id}")
    public ResponseEntity<GoodsDetailDto>selrctGoods(@PathVariable Long id) {
        GoodsDetailDto goodsDetailDto = goodsService.selrctGoods(id);
        return ResponseEntity.ok(goodsDetailDto);
    }
        // 굿즈 등록
        @PostMapping("/new")
        public ResponseEntity<Boolean> insertGoods(@RequestBody GoodsDetailDto goodsDetailDto) {
            boolean  list = goodsService.insertGoods(goodsDetailDto);
            return ResponseEntity.ok(list);
        }
        // 굿즈 수정
        @PutMapping("/modify/{id}")
        public ResponseEntity<Boolean> updateGoods(@PathVariable Long id, @RequestBody GoodsDetailDto goodsDetailDto) {
            boolean isTrue = goodsService.updateGoods(id, goodsDetailDto);
            return ResponseEntity.ok(isTrue);
        }

















    }











