package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GoodsRepository extends JpaRepository<GoodsDetail, Long> {
    Page<GoodsDetail> findAll(Pageable pageable);   // 전체 조회
}
