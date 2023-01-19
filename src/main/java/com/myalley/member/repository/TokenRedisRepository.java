package com.myalley.member.repository;

import com.myalley.member.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRedisRepository extends CrudRepository<RefreshToken,String> {
    Optional<RefreshToken> findById(String email);
}
