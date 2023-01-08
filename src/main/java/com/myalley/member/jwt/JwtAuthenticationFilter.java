package com.myalley.member.jwt;

import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        // 로그인할 때 입력한 username과 password를 가지고 authenticationToken를 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"),
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
        // 쿠키 생성
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("access_token", token.get("accessToken"));
        response.setHeader("refresh_token", token.get("refreshToken"));
        // response.setContentType(JwtProperties.COOKIE_NAME);
        // new ObjectMapper().writeValue(response.getWriter(),token);
//        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, token.toString());
//        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
//        cookie.setPath("/");
        //  response.addCookie(cookie);



        // new ObjectMapper().writeValue(response.getWriter(), token);
        response.sendRedirect("/");

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException {

        response.sendRedirect("/login");
    }
}