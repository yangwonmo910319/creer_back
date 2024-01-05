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
    public Long addToCart(CartDto cartDto) {
        try {
            Long buyerId = getCurrentMemberId(); // 구매자
            Member buyer = memberRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다!"));
            GoodsDetail goodsDetail = goodsRepository.findById(cartDto.getGoodsDetailId()).orElseThrow(() -> new RuntimeException("상품 아이디가 존재하지 않습니다!")); // 중첩 DTO            log.warn("{}" + goodsDetail);
            Member seller = memberRepository.findById(goodsDetail.getMember().getId()).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다!"));
            Cart cart = Cart.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .goodsDetailId(cartDto.getGoodsDetailId())
                    .option(cartDto.getOption())
                    .quantity(cartDto.getQuantity())
                    .title(cartDto.getTitle())
                    .goodsImg(cartDto.getGoodsImg())
                    .price(cartDto.getPrice())
                    .build();

            Cart savedCart = cartRepository.save(cart);
            return savedCart.getCartId();
        } catch (Exception e) {
            throw new RuntimeException("CartService addToCart 에서 오류가 발생했습니다! : ", e);
        }
    }
    // 장바구니에 추가
    @Transactional
    public Long addToCart2(CartDto cartDto,String buyer) {
        try {
            Member member = memberRepository.findByNickName(buyer).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다!"));
            GoodsDetail goodsDetail = goodsRepository.findById(cartDto.getGoodsDetailId()).orElseThrow(() -> new RuntimeException("상품 아이디가 존재하지 않습니다!")); // 중첩 DTO            log.warn("{}" + goodsDetail);
            Member seller = memberRepository.findById(goodsDetail.getMember().getId()).orElseThrow(() -> new RuntimeException("구매자 아이디가 없습니다!"));
            Cart cart = Cart.builder()
                    .buyer(member)
                    .seller(seller)
                    .goodsDetailId(cartDto.getGoodsDetailId())
                    .option(cartDto.getOption())
                    .quantity(cartDto.getQuantity())
                    .title(cartDto.getTitle())
                    .goodsImg(cartDto.getGoodsImg())
                    .price(goodsDetail.getGoodsPrice())
                    .build();
            Cart savedCart = cartRepository.save(cart);
            return savedCart.getCartId();
        } catch (Exception e) {
            throw new RuntimeException("CartService addToCart 에서 오류가 발생했습니다! : ", e);
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

    public CartDto select(Long id) {
        Long memberId = getCurrentMemberId();
        if (memberId == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 회원이 카트가 없습니다."));

        // Entity의 데이터를 DTO로 매핑해서 반환 (Entity -> DTO)
        return modelMapper.map(cart, CartDto.class);
    }

    // 상품 삭제
    @Transactional
    public boolean delete(Long id) {
        try {
            cartRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
