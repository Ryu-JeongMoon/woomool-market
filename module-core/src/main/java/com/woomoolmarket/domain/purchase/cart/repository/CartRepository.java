package com.woomoolmarket.domain.purchase.cart.repository;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c join fetch c.product where c.member = :member")
    List<Cart> findAllByMember(Member member);

    void deleteAllByMemberId(Long memberID);
}
