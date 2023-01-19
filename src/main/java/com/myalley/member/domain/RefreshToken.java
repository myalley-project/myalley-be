package com.myalley.member.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@AllArgsConstructor
@RedisHash(value="refreshToken",timeToLive=600)
public class RefreshToken {

    @Id
    private String email;

    private String token;


}
