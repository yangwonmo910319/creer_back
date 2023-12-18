package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPurchase;
import com.team.creer_back.entity.goods.GoodsReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PurchaseRepository extends JpaRepository<GoodsPurchase, Long> {


}
