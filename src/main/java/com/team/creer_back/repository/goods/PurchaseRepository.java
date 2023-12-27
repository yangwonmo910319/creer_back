package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPurchase;
import com.team.creer_back.entity.goods.GoodsReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<GoodsPurchase, Long> {

    Optional<List<GoodsPurchase>> findByBuyerId(Long memberId);
    Optional<List<GoodsPurchase>> findBySellerId(Long memberId);
}
