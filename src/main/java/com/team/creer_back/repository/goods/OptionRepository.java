package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsDetail;
import com.team.creer_back.entity.goods.GoodsOption;
import com.team.creer_back.entity.goods.GoodsPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OptionRepository extends JpaRepository<GoodsOption, Long> {

}
