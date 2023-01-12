package com.myalley.member.service;

import com.myalley.member.domain.RefreshToken;
import com.myalley.member.repository.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenRedisService {

    private final TokenRedisRepository tokenRedisRepository;
    @Transactional
    public void saveToken(Long userId, String token){
        try{
            tokenRedisRepository.save(RefreshToken.builder()
                    .userId(userId)
                    .token(token)
                    .build());
        }catch(Exception e){
            log.error(e.getMessage());
        }

    }

    public String getToken(Long userId){
        try{
            return tokenRedisRepository.findById(userId)
                    .orElseThrow(IllegalArgumentException::new)
                    .getToken();
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    @Transactional
    public void deleteToken(RefreshToken token){
        try{
            tokenRedisRepository.delete(token);

        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
}
