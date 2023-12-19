package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsPicture;
import com.team.creer_back.entity.goods.GoodsReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<GoodsPicture, Long> {


    List<GoodsPicture> findByGoodsDetail(GoodsDetail goodsDetail);
}
