package com.myalley.member.repository;

import com.myalley.member.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<RefreshToken,Long> {

}
