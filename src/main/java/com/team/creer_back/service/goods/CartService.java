package com.team.creer_back.service.goods;

import com.team.creer_back.dto.goods.CartDto;
import com.team.creer_back.entity.goods.Cart;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.CartRepository;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository;

    public CartService(CartRepository cartRepository, MemberRepository memberRepository, GoodsRepository goodsRepository){
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.goodsRepository = goodsRepository;
    }

    // 장바구니에 추가
    @Transactional
    public boolean addToCart(CartDto cartDto) {
        try {
            Member member = memberRepository.findById(cartDto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
            // orElseThrow : Optional 객체가 비어있을 경우, 즉 NULL일 경우에 예외를 발생시킨다.

            GoodsDetail goodsDetail = goodsRepository.findById(cartDto.getGoodsDetailId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

            Cart cart = Cart.builder()
                    .member(member)
                    .goodsDetail(goodsDetail)
                    .build();

            cartRepository.save(cart);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public List<CartDto> getCartItems(Long memberId) {
        List<Cart> cartItems = cartRepository.findByMemberId(memberId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        // Entity 의 데이터를 DTO 로 매핑하는데 사용하는 라이브러리
        // Collecter는?
        ModelMapper modelMapper = new ModelMapper();
        return cartItems.stream()
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .collect(Collectors.toList());
    }
}
