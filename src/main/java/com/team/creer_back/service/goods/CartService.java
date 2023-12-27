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
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, MemberRepository memberRepository, GoodsRepository goodsRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.goodsRepository = goodsRepository;
        this.modelMapper = modelMapper;
    }

    // 장바구니에 추가
    @Transactional
    public boolean addToCart(CartDto cartDto) {
        try {
            Long buyerId = getCurrentMemberId(); // 구매자
            Member buyer = memberRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다!"));
            GoodsDetail goodsDetail = goodsRepository.findById(Long.valueOf(261)).orElseThrow(() -> new RuntimeException("상품 아이디가 존재하지 않습니다!"));
            Member seller = memberRepository.findById(goodsDetail.getMember().getId()).orElseThrow(() -> new RuntimeException("판매자 아이디가 없습니다!"));
            Cart cart = Cart.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .goodsDetail(goodsDetail)
                    .option(cartDto.getOption())
                    .quantity(cartDto.getQuantity())
                    .build();

            cartRepository.save(cart); // 저장하는 로직 추가

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CartDto> getCartItems(Long memberId) {
        if (memberId == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        List<Cart> cartList = cartRepository.findByBuyer_Id(memberId);
        if (cartList.isEmpty()) {
            throw new RuntimeException("장바구니에 물품이 존재하지 않습니다.");
        }

        // Entity의 데이터를 DTO로 매핑해서 반환 (Entity -> DTO)
        return cartList.stream()
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .collect(Collectors.toList());
    }
}
