package com.woomoolmarket.domain.purchase.cart.repository;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

  @Query("select c from Cart c join fetch c.member join fetch c.product where c.member = :member")
  List<Cart> findByMember(Member member);

  @Query("select c from Cart c join fetch c.member join fetch c.product where c.id in :cartIds")
  List<Cart> findByIds(Collection<Long> cartIds);

  @Modifying
  void deleteById(Long id);

  @Modifying
  @Query("delete from Cart c where c.id in :cartIds")
  void deleteByIds(Collection<Long> cartIds);

  @Modifying
  @Query("delete from Cart c where c.member = :member")
  void deleteByMember(Member member);
}
