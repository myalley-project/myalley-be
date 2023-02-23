package com.myalley.member.jwt;

import com.myalley.member.domain.Member;
import com.myalley.member.domain.RefreshToken;
import com.myalley.member.repository.TokenRedisRepository;
import com.myalley.member.service.RefreshService;
import io.jsonwebtoken.*;


import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    /**
     * 토큰에서 username 찾기
     *
     * @param token 토큰
     * @return email
     */

    public static String SECRET_KEY;

    @Value("${secret.JWT_SECRET_KEY}")
    public void setKey(String key){
        SECRET_KEY=key;
    }


    public static String getEmail(String token) {
        // jwtToken에서 email을 찾습니다.
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // username

    }



    /**
     * user로 토큰 생성
     * HEADER : alg, kid
     * PAYLOAD : sub, iat, exp
     * SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시
     *
     * @param member 유저
     * @return jwt token
     */
    public static String createToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getUsername()); // subject
        Date now = new Date(); // 현재 시간
       // Pair<String, Key> key = JwtKey.getRandomKey();
        // JWT Token 생성

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime()+ JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정
                .setHeaderParam(JwsHeader.KEY_ID, "JWT") // kid
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),SignatureAlgorithm.HS256) // signature
                .compact();
    }
    //refresh,acess모두 생성
    public static Map<String,String> createTokenSet(Member member) {
        Map<String,String> tokens=new HashMap<String,String>();
        Claims claims = Jwts.claims().setSubject(member.getUsername()); // subject
        Date now = new Date(); // 현재 시간
       // Pair<String, Key> key = JwtKey.getRandomKey();
        // JWT Token 생성
        tokens.put("accessToken",Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime()+ JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정
                .setHeaderParam(JwsHeader.KEY_ID, "JWT") // kid
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),SignatureAlgorithm.HS256) // signature
                .compact());
        tokens.put("refreshToken",Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime()+ JwtProperties.REFRESH_EXPIRATION_TIME)) // 토큰 만료 시간 설정
                .setHeaderParam(JwsHeader.KEY_ID, "JWT") // kid
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),SignatureAlgorithm.HS256) // signature
                .compact());

        return tokens;
    }
    //acess 토큰 만료시 refresh access 모두 새로 생성성
}
