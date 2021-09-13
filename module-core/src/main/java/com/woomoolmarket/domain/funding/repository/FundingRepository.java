package com.woomoolmarket.domain.funding.repository;

import com.woomoolmarket.domain.funding.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRepository extends JpaRepository<Funding, Long> {

}
