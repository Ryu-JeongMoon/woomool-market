package com.woomoolmarket.domain.token.repository;

import com.woomoolmarket.domain.token.entiy.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByKey(String key);
}
