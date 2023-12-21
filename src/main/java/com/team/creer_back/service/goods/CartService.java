package com.team.creer_back.service.goods;

import com.team.creer_back.dto.goods.CartDto;
import com.team.creer_back.entity.goods.Cart;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.member.Member;
import com.team.creer_back.repository.goods.CartRepository;
import com.team.creer_back.repository.goods.GoodsRepository;
import com.team.creer_back.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.team.creer_back.security.SecurityUtil.getCurrentMemberId;

@Transactional(readOnly = true)
@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository;

    public CartService(CartRepository cartRepository, MemberRepository memberRepository, GoodsRepository goodsRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.goodsRepository = goodsRepository;
    }

    // 장바구니에 추가
    @Transactional
    public boolean addToCart(CartDto cartDto) {
        try {
            Long buyerId = getCurrentMemberId(); // 구매자
            Member buyer = memberRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다!"));
            Member seller = memberRepository.findById(cartDto.getSeller()).orElseThrow(() -> new RuntimeException("판매자 아이디가 없습니다!"));
            GoodsDetail goodsDetail = goodsRepository.findById(cartDto.getGoodsDetailId()).orElseThrow(() -> new RuntimeException("상품 아이디가 존재하지 않습니다!"));

            Cart cart = Cart.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .goodsDetail(goodsDetail)
                    .option(cartDto.getOption())
                    .quantity(cartDto.getQuantity())
                    .status("결제 전")
                    .build();

            cartRepository.save(cart); // 저장하는 로직 추가

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CartDto> getCartItems(Long memberId) {
        List<Cart> cartItems = cartRepository.findByBuyer_Id(memberId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        // 아래 오류 수정~~~~~~~~~~~~~~~~~
        // Entity 의 데이터를 DTO 로 매핑하는데 사용하는 라이브러리
        // Collecter는?
        ModelMapper modelMapper = new ModelMapper();
        return cartItems.stream()
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .collect(Collectors.toList());
    }
}
