package com.myalley.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myalley.exception.CustomException;
import com.myalley.exception.MemberExceptionType;
import com.myalley.member.domain.Member;
import com.myalley.member.domain.RefreshToken;
import com.myalley.member.dto.LoginDto;
import com.myalley.member.repository.MemberRepository;
import com.myalley.member.repository.TokenRedisRepository;
import com.myalley.member.service.MemberService;
import com.myalley.member.service.RedisService;
import com.myalley.member.service.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final RedisService redisService;


    /**
     * 로그인 인증 시도
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        ServletInputStream inputStream = null;
        LoginDto loginDto;
        try {
            inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = objectMapper.readValue(messageBody, LoginDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword(),
                new ArrayList<>()
        );
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {

        Member member = (Member) authResult.getPrincipal();
        Map<String,String> token = JwtUtils.createTokenSet(member);
        redisService.save(member.getEmail(),token.get("refreshToken"));
        response.setContentType("application/json");


       new ObjectMapper().writeValue(response.getWriter(),token);

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException {
        response.setStatus(404);
        response.setContentType("application/json");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorCode", 404);
        body.put("errorMsg", "회원 정보 없음");

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}