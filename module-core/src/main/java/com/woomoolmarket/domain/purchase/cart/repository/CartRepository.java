package com.woomoolmarket.domain.purchase.cart.repository;

import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.member = :memberId")
    List<Cart> findByMemberId(Long memberId);
}
