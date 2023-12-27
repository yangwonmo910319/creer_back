package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.service.goods.GoodsService;
import com.team.creer_back.service.goods.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService; // 생성자를 통해서 값을 참조할 수 있음
    private final PictureService pictureService; // 생성자를 통해서 값을 참조할 수 있음
    // 상품 태그 조회
    @GetMapping("/list/tag")
    public ResponseEntity<List<GoodsDetailDto>> tagGoods(@RequestParam String  keyword){
        List<GoodsDetailDto> list = goodsService.tagGoods(keyword);
        return ResponseEntity.ok(list);
    }
    // 상품 제목 조회
    @GetMapping("/list/title")
    public ResponseEntity<List<GoodsDetailDto>>titleGoods(@RequestParam String  keyword){
        List<GoodsDetailDto> list = goodsService.TitleGoods(keyword);
        return ResponseEntity.ok(list);
    }
    // 상품 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<GoodsDetailDto>> goodsList() {
        List<GoodsDetailDto> list = goodsService.getGoodsList();
        return ResponseEntity.ok(list);
    }
    // 상품 하나 조회
    @GetMapping("/list/{id}")
    public ResponseEntity<GoodsDetailDto> goods(@PathVariable Long id){
        GoodsDetailDto list = goodsService.getGoods(id);
        return ResponseEntity.ok(list);
    }
    // 내가 등록한 상품만 조회
    @GetMapping("/Mylist")
    public ResponseEntity<List<GoodsDetailDto>> getMyGoods(){
        List<GoodsDetailDto> list  = goodsService.getMyGoods();
        return ResponseEntity.ok(list);
    }

    // 상품 등록
    @PostMapping("/new")
    public ResponseEntity<Long> insertGoods(@RequestBody GoodsDetailDto goodsDetailDto) {
        Long list = goodsService.insertGoods(goodsDetailDto);
        return ResponseEntity.ok(list);
    }
    // 상품 이미지 등록
    @PostMapping("/new/picture")
    public ResponseEntity<Boolean> insertGoodsPicture(@RequestBody GoodsDetailDto goodsDetailDto) {
        boolean list = goodsService.insertPicture(goodsDetailDto);
        return ResponseEntity.ok(list);
    }
    //상품 이미지 한장 삭제
    @DeleteMapping("/delete/picture/{goodsPictureId}")
    public ResponseEntity<Boolean> deleteGoodsPicture(@PathVariable Long goodsPictureId) {
        boolean result = pictureService.deletePicture(goodsPictureId);
        return ResponseEntity.ok(result);
    }

    //상품 이미지 출력
    @GetMapping("/select/picture/{num}")
    public ResponseEntity<List<GoodsPictureDto>> updateGoodsPicture(@PathVariable Long num) {
        List<GoodsPictureDto> list= pictureService.removeAndRetrievePictures(num);
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


    // 페이지네이션
    @GetMapping("/list/page")
    public ResponseEntity<List<GoodsDetailDto>> goodsList (@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<GoodsDetailDto> list = goodsService.getMovieList(page, size);
        log.info("list : {}", list);
        return ResponseEntity.ok(list);
    }

    // 페이지 수 조회
    @GetMapping("/list/count")
    public ResponseEntity<Integer> goodsListCount(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size  ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = goodsService.getMoviePage(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    @PostMapping("/insert")
    public ResponseEntity<Boolean> goodsInsert(@RequestBody List<Map<String, String>> goodsList) {
        log.info("movieList : {}", goodsList);
        Member member  = new Member();
        member.setId(Long.valueOf(1));
        for(Map<String, String> data : goodsList) {
            GoodsDetail  goodsDetail = new GoodsDetail();
            goodsDetail. setGoodsCategory("뷰티");
            goodsDetail.setGoodsPic(data.get("image"));
            goodsDetail.setGoodsDesc("");
            goodsDetail.setGoodsStock(1000L);
            goodsDetail.setGoodsTitle(data.get("title"));
            goodsDetail.setMember(member);
            goodsDetail.setGoodsPrice(Long.valueOf(data.get("price")));
            goodsDetail.setGoodsDeliveryFee("3000원");
            goodsDetail.setGoodsStatus("sale");
            goodsService.saveGoods(goodsDetail);
        }
        return ResponseEntity.ok(true);
    }
}