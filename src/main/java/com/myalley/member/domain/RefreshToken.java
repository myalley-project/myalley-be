package com.myalley.member.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@AllArgsConstructor
@RedisHash(value="refreshToken",timeToLive=60*60*24*30)
public class RefreshToken {

    @Id
    private String email;

    private String token;


}
