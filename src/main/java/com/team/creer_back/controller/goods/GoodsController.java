package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.service.goods.GoodsService;
import com.team.creer_back.service.goods.PictureService;
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
    private final PictureService pictureService; // 생성자를 통해서 값을 참조할 수 있음
    // 상품 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<GoodsDetailDto>> goodsList() {
        List<GoodsDetailDto> list = goodsService.getGoodsList();
        return ResponseEntity.ok(list);
    }
    // 상품 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> insertGoods(@RequestBody GoodsDetailDto goodsDetailDto) {
        boolean list = goodsService.insertGoods(goodsDetailDto);
        return ResponseEntity.ok(list);
    }
    // 상품 이미지 등록
    @PostMapping("/new/picture")
    public ResponseEntity<Boolean> insertGoodsPicture(@RequestBody GoodsPictureDto goodsPictureDto) {
        boolean list = pictureService.insertPicture(goodsPictureDto);

        return ResponseEntity.ok(list);
    }
    //상품 이미지 한장 삭제
    @DeleteMapping("/delete/picture/{goodsPictureId}")
    public ResponseEntity<Boolean> deleteGoodsPicture(@PathVariable Long goodsPictureId) {
        boolean result = pictureService.deletePicture(goodsPictureId);
        return ResponseEntity.ok(result);
    }
    //상품 이미지 수정
//    @PutMapping("/update/picture/{goodsPictureId}")
//    public ResponseEntity<Boolean> updateGoodsPicture(@PathVariable Long goodsPictureId, @RequestBody GoodsPictureDto goodsPictureDto) {
//        boolean result = pictureService.updatePicture(goodsPictureId, goodsPictureDto);
//        return ResponseEntity.ok(result);
//    }

    //상품 이미지 출력
    @GetMapping("/select/picture/{num}")
    public ResponseEntity<List<String>> updateGoodsPicture(@PathVariable Long num) {
        List<String> list= pictureService.removeAndRetrievePictures(num);
        return ResponseEntity.ok(list);
    }





    // 상품 삭제
    @GetMapping("/delete/{num}")
    public ResponseEntity<Boolean> deleteGoods(@PathVariable Long num) {
        boolean list = goodsService.deleteGoods(num);
        return ResponseEntity.ok(list);
    }

    // 상품 수정
    @PostMapping("/update/{num}")
    public ResponseEntity<Boolean> updateGoods(@PathVariable Long num, @RequestBody GoodsDetailDto goodsDetailDto) {
        boolean list = goodsService.updateGoods(num,goodsDetailDto);
        return ResponseEntity.ok(list);
    }

}