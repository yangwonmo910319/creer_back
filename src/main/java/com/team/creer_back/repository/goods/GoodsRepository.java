package com.team.creer_back.repository.goods;


import com.team.creer_back.entity.goods.GoodsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<GoodsDetail, Long> {
    Page<GoodsDetail> findAll(Pageable pageable);   // 전체 조회
    List<GoodsDetail> findBygoodsTitleContaining(String keyword);  //글 제목 검색
    List<GoodsDetail> findBygoodsCategoryContaining(String keyword);  //글 테그 검색
}