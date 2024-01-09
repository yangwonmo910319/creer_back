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
@RequestMapping("purchase")
@RequiredArgsConstructor
public  class PurchaseController {
    private final PurchaseService purchaseService; // 생성자를 통해서 값을 참조할 수 있음

    //구매 목록 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> insertPicture(@RequestParam Long num ,@RequestBody GoodsPurchaseDto goodsPurchaseDto) {
        boolean list = purchaseService.insertPurchase(num,goodsPurchaseDto);
        return ResponseEntity.ok(list);
    }

    //결제 상황 변경
    @PostMapping("/update")
    public ResponseEntity<Boolean> update(@RequestParam int id,@RequestParam String content) {
        boolean list = purchaseService.updatePurchase(id,content);
        return ResponseEntity.ok(list);
    }
    // 구매 목록 출력
    @GetMapping("/buy")
    public ResponseEntity<List<GoodsPurchaseDto>> selectBuyer() {
        List<GoodsPurchaseDto> list = purchaseService.selectBuyer();
        return ResponseEntity.ok(list);
    }
    //판매 목록 출력
    @GetMapping("/seller")
    public ResponseEntity<List<GoodsPurchaseDto>> selectSeller() {
        List<GoodsPurchaseDto> list = purchaseService.selectSeller();
        return ResponseEntity.ok(list);
    }
}











