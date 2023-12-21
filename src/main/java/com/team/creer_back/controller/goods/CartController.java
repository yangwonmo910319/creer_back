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
@RequestMapping("Cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 장바구니에 추가
    @PostMapping("/add")
    public ResponseEntity<Boolean> addToCart(@RequestBody CartDto cartDto){
        boolean list = cartService.addToCart(cartDto);
        return ResponseEntity.ok(list);
    }

     // 장바구니 목록
    @GetMapping("/list")
    public ResponseEntity<List<CartDto>> getCartItems(@RequestHeader("Authorization") String accessToken) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<CartDto> cartItems = cartService.getCartItems(memberId);

        return ResponseEntity.ok(cartItems);
    }
}
