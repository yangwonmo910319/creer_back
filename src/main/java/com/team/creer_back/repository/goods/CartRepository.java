package com.team.creer_back.repository.goods;

import com.team.creer_back.entity.goods.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByBuyer_Id(Long buyerId);
}
