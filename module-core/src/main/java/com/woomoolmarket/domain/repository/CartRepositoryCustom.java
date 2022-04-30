package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.repository.querydto.CartQueryResponse;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartRepositoryCustom {

  Page<CartQueryResponse> findPickedBy(Collection<Long> cartIds, Pageable pageable);

  Page<CartQueryResponse> searchBy(Long memberId, Pageable pageable);

  Page<CartQueryResponse> searchForAdminBy(Pageable pageable);
}
