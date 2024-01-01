package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsAutcionDto;
import com.team.creer_back.dto.goods.GoodsDetailDto;
import com.team.creer_back.dto.goods.GoodsPictureDto;
import com.team.creer_back.entity.goods.GoodsAution;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.service.goods.AuctionService;
import com.team.creer_back.service.goods.GoodsService;
import com.team.creer_back.service.goods.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("auction")
@RequiredArgsConstructor
public class AuctionController {
    private final GoodsService goodsService; // 생성자를 통해서 값을 참조할 수 있음
    private final AuctionService goodsAution; // 생성자를 통해서 값을 참조할 수 있음


    // 시간 등록
    @PostMapping("/new/{auctionTime}")
    public ResponseEntity<Boolean> titleGoods(@RequestBody GoodsDetailDto goodsDetail,@PathVariable String auctionTime) {
          Long num = goodsService.insertGoods(goodsDetail);
        String isoDateTimeString = auctionTime; // 클라이언트에서 전송한 ISO 8601 형식의 문자열
        LocalDateTime dateTime = LocalDateTime.parse(isoDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
        Boolean list = goodsAution.insertAuction(num,dateTime);
        return ResponseEntity.ok(true);
    }


}