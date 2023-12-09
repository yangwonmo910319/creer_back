package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsReviewDto;
import com.team.creer_back.entity.goods.GoodsReview;
import com.team.creer_back.service.goods.GoodsService;
import com.team.creer_back.service.goods.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.util.impl.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/Review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService; // 생성자를 통해서 값을 참조할 수 있음
            // 리뷰 등록
        @PostMapping("/new")
        public ResponseEntity<Boolean> insertReview(@RequestBody GoodsReviewDto goodsDetailDto) {
            boolean  list = reviewService.insertReview(goodsDetailDto);
            log.warn("insertReplyinsertReplyinsertReplyinsertReplyinsertReplyinsertReplyinsertReply");
            return ResponseEntity.ok(list);
        }

    // 리뷰 전체 조회
    @GetMapping("/list/{num}")
    public ResponseEntity<List<GoodsReviewDto>> selctReviewList(@PathVariable Long num) {
        List<GoodsReviewDto> list = reviewService.getReviewList(num);
        return ResponseEntity.ok(list);
    }
    // 리뷰 삭제
    @GetMapping("/delete/{num}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable Long num) {
        boolean list = reviewService.deleteReview(num);
        return ResponseEntity.ok(list);
    }

    // 리뷰 수정
    @PostMapping("/update/{num}")
    public ResponseEntity<Boolean> updateReview(@PathVariable Long num, @RequestBody GoodsReviewDto goodsDetailDto) {
        boolean list = reviewService.updateReview(num,goodsDetailDto);
        return ResponseEntity.ok(list);
    }







}











