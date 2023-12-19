package com.team.creer_back.controller.goods;

import com.team.creer_back.entity.goods.Cart;
import com.team.creer_back.service.goods.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("Cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping("/{memberId}")
    public List<Cart> getCartItems(@PathVariable Long memberId) {
        return cartService.getCartItems(memberId);
    }
}
