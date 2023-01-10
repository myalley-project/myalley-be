package com.myalley.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myalley.member.domain.Member;
import com.myalley.member.dto.LoginDto;
import com.myalley.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//        this.authenticationManager = authenticationManager;
//    }

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
    /**
     * 인증에 성공했을 때 사용
     * JWT Token을 생성해서 쿠키에 넣는다.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {


        Member member = (Member) authResult.getPrincipal();
        //String token= JwtUtils.createToken(user);
        Map<String,String> token = JwtUtils.createTokenSet(member);

        response.setContentType("application/json");
        //response.setCharacterEncoding("utf-8");
//        response.setHeader("access_token", token.get("accessToken"));
//        response.setHeader("refresh_token", token.get("refreshToken"));

       // new ObjectMapper().writeValue(response.getWriter(),token);

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException {
//임시 예외처리

        response.sendRedirect("/login");
    }
}