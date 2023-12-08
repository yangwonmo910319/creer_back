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

@Slf4j
@RestController
@RequestMapping("api/Review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService; // 생성자를 통해서 값을 참조할 수 있음
            // 굿즈 리플 등록
        @PostMapping("/new")
        public ResponseEntity<Boolean> insertReply(@RequestBody GoodsReviewDto goodsDetailDto) {
            boolean  list = reviewService.insertReview(goodsDetailDto);
            log.warn("insertReplyinsertReplyinsertReplyinsertReplyinsertReplyinsertReplyinsertReply");
            return ResponseEntity.ok(list);
        }
//
//    // 굿즈 전체 조회
//    @GetMapping("/list")
//    public ResponseEntity<List<GoodsDetailDto>> selctGoodsList() {
//        List<GoodsDetailDto> list = goodsService.getGoodsList();
//        return ResponseEntity.ok(list);
//    }
//    // 굿즈 하나 조회
//    @GetMapping("/listDetail/{id}")
//    public ResponseEntity<GoodsDetailDto>selrctGoods(@PathVariable Long id) {
//        GoodsDetailDto goodsDetailDto = goodsService.selrctGoods(id);
//        return ResponseEntity.ok(goodsDetailDto);
//    }
//
//    // 굿즈 테그 필터
//    @GetMapping("/listDetail/Category/{keyword}")
//    public ResponseEntity <List<GoodsDetailDto>>  selctGoodsCategory(@PathVariable  String keyword) {
//        List<GoodsDetailDto>list =  goodsService.selctGoodsCategory(keyword);
//        return ResponseEntity.ok(list);
//    }
//
//    // 굿즈 제목 필터
//    @GetMapping("/listDetail/Title/{keyword}")
//    public ResponseEntity <List<GoodsDetailDto>> selctGoodsTitle(@RequestParam String keyword) {
//        List<GoodsDetailDto>list = goodsService.selctGoodsTitle(keyword);
//        return ResponseEntity.ok(list);
//    }
//

//
//
//    // 굿즈 수정
//    @PutMapping("/modify/{id}")
//    public ResponseEntity<Boolean> updateGoods(@PathVariable Long id, @RequestBody GoodsDetailDto goodsDetailDto) {
//        boolean isTrue = goodsService.updateGoods(id, goodsDetailDto);
//        return ResponseEntity.ok(isTrue);
//    }
//
//
//












    }











