package com.team.creer_back.controller.goods;

import com.team.creer_back.dto.goods.CartDto;
import com.team.creer_back.security.SecurityUtil;
import com.team.creer_back.service.goods.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 장바구니에 추가
    @PostMapping("/add")
    public ResponseEntity<Long> insertCart(@RequestBody CartDto cartDto){
        Long list = cartService.insertCart(cartDto);
        return ResponseEntity.ok(list);
    }

    // 경매로 장바구니 추가
    @PostMapping("/add2/{buyer}")
    public ResponseEntity<Long> insertCart2(@RequestBody CartDto cartDto ,@PathVariable String buyer){
        Long list = cartService.insertCart2(cartDto,buyer);
        return ResponseEntity.ok(list);
    }
     // 장바구니 목록 출력
    @GetMapping("/list")
    public ResponseEntity<List<CartDto>> selectCart(@RequestHeader("Authorization") String accessToken) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<CartDto> cartItems = cartService.selectCartList(memberId);
        return ResponseEntity.ok(cartItems);
    }

    // 장바구니  제거
    @DeleteMapping("/delete/{num}")
    public ResponseEntity<Boolean> deleteGoodsPicture(@PathVariable Long num) {
        boolean result = cartService.deleteCart(num);
        return ResponseEntity.ok(result);
    }

    // 장바구니 하나 출력
    @GetMapping("/select/{num}")
    public   ResponseEntity<CartDto> selectGoodsPicture(@PathVariable Long num) {
       CartDto result = cartService.selectCart(num);
        return ResponseEntity.ok(result);
    }
}
