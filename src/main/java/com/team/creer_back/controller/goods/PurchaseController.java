package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.GoodsPurchaseDto;
import com.team.creer_back.service.goods.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/purchase")
@RequiredArgsConstructor
public  class PurchaseController {
    private final PurchaseService purchaseService; // 생성자를 통해서 값을 참조할 수 있음

    //구매 목록 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> insertPicture(@RequestBody GoodsPurchaseDto goodsPurchaseDto) {
        boolean list = purchaseService.insertPurchase(goodsPurchaseDto);
        return ResponseEntity.ok(list);
    }

    //구매 목록 출력
    @GetMapping("/list")
    public ResponseEntity<List<GoodsPurchaseDto>> selectPicture() {
        List<GoodsPurchaseDto> list = purchaseService.SelectPicture();
        return ResponseEntity.ok(list);
    }
}











