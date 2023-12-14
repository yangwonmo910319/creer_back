package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.service.goods.PictureService;
import com.team.creer_back.service.goods.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


    @Slf4j
    @RestController
    @RequestMapping("api/picture")
    @RequiredArgsConstructor
    public  class PictureController {
        private final PictureService pictureService; // 생성자를 통해서 값을 참조할 수 있음

                //사진 등록
            @PostMapping("/new")
            public ResponseEntity<Boolean> insertPicture(@RequestBody GoodsPictureDto goodsPictureDto) {
                boolean  list = pictureService.insertPicture(goodsPictureDto);
                return ResponseEntity.ok(list);
            }
        //사진 수정
        @PostMapping("/update")
        public ResponseEntity<Boolean> updatePicture(@RequestBody GoodsPictureDto goodsPictureDto) {
            boolean  list = pictureService.updatePicture(goodsPictureDto);
            return ResponseEntity.ok(list);
        }

        // 사진 전체 조회
        @GetMapping("/list/{num}")
        public ResponseEntity<List<GoodsPictureDto>> selectReviewList(@PathVariable Long num) {
            List<GoodsPictureDto> list = pictureService.removeAndRetrievePictures(num);
            return ResponseEntity.ok(list);
        }

        //상품 이미지 한장 삭제
        @DeleteMapping("/delete/{num}")
        public ResponseEntity<Boolean> deleteGoodsPicture(@PathVariable Long num) {
            boolean result = pictureService.deletePicture(num);
            return ResponseEntity.ok(result);

    }
}











