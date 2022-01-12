package com.woomoolmarket.domain.purchase.cart.repository;

import com.woomoolmarket.domain.purchase.cart.query.CartQueryResponse;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartRepositoryCustom {

  Page<CartQueryResponse> findPickedBy(Collection<Long> cartIds, Pageable pageable);

  Page<CartQueryResponse> searchBy(Long memberId, Pageable pageable);

  Page<CartQueryResponse> searchForAdminBy(Pageable pageable);
}
