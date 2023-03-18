package com.myalley.member.service;

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

    public void saveRefreshToken(String email, String refreshToken){

        tokenRedisRepository.save(new RefreshToken(email,refreshToken));
    }

    public void deleteRefreshToken(String email){

            tokenRedisRepository.deleteById(email);



    }

}
