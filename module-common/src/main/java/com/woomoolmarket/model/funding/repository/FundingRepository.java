package com.woomoolmarket.model.funding.repository;

import com.woomoolmarket.model.funding.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRepository extends JpaRepository<Funding, Long> {

}
