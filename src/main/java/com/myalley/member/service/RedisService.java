package com.myalley.member.service;

import com.myalley.member.domain.Member;
import com.myalley.member.domain.RefreshToken;
import com.myalley.member.repository.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final TokenRedisRepository tokenRedisRepository;

    public void save(String email,String refreshToken){

        tokenRedisRepository.save(new RefreshToken(email,refreshToken));
    }

}
