package org.example.expert.domain.auth.repository;

import org.example.expert.domain.auth.entity.RedisRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisRefreshTokenRepository extends CrudRepository<RedisRefreshToken, Long> {
}
