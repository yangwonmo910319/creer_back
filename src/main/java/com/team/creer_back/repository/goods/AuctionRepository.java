package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsAution;
import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<GoodsAution, Long> {



}
