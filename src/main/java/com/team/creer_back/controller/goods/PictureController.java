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
@RequestMapping("api/Picture")
@RequiredArgsConstructor
public class PictureController {
    private final ReviewService reviewService; // 생성자를 통해서 값을 참조할 수 있음
    private final PictureService pictureService; // 생성자를 통해서 값을 참조할 수 있음
            // 리뷰 등록
        @PostMapping("/new")
        public ResponseEntity<Boolean> insertReview(@RequestBody GoodsReviewDto goodsDetailDto) {
            boolean  list = reviewService.insertReview(goodsDetailDto);
            return ResponseEntity.ok(list);
        }

    // 사진 전체 조회
    @GetMapping("/list/{num}")
    public ResponseEntity<List<GoodsPictureDto>> selctReviewList(@PathVariable Long num) {
        List<GoodsPictureDto> list = pictureService.removeAndRetrievePictures(num);
        return ResponseEntity.ok(list);
    }


}











