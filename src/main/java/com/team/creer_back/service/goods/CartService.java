package com.team.creer_back.service.goods;

import com.team.creer_back.entity.goods.Cart;
import com.team.creer_back.repository.goods.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCartItems(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }
}
