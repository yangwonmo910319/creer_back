package com.team.creer_back.controller.goods;


import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.service.goods.GoodsService;
import com.team.creer_back.service.goods.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("auction")
@RequiredArgsConstructor
public class AuctionController {

    private final GoodsService goodsService; // 생성자를 통해서 값을 참조할 수 있음

    // 시간 등록
    @PostMapping("/new/{auctionTime}")
    public ResponseEntity<Long> titleGoods(@RequestBody GoodsDetailDto goodsDetail, @PathVariable String auctionTime) {
        String isoDateTimeString = auctionTime; // 클라이언트에서 전송한 ISO 8601 형식의 문자열
        LocalDateTime dateTime = LocalDateTime.parse(isoDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
        Long list = goodsService.insertGoods(goodsDetail,auctionTime);
        return ResponseEntity.ok(list);
    }


}