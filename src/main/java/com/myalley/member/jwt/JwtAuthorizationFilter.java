package com.myalley.member.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myalley.exception.CustomException;
import com.myalley.exception.MemberExceptionType;
import com.myalley.member.domain.Member;
import com.myalley.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String servletPath = request.getServletPath();
        // header 에서 JWT token을 가져옵니다.
        String authorizationHeader = request.getHeader("AUTHORIZATION");

        try {
            // Access Token만 꺼내옴
            String accessToken = null;
            if (authorizationHeader != null)
                accessToken = authorizationHeader.substring("Bearer ".length());

            // === Access Token 검증 === //
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JwtSecret.JWT_SECRET_KEY)).build();
            DecodedJWT decodedJWT = verifier.verify(accessToken);

            String username = decodedJWT.getSubject();

            Member mem = memberRepository.findByEmail(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(mem, null, mem.getAuthorities());//authority
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            log.info("Access Token이 만료되었습니다.");
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            CustomException customException = new CustomException(MemberExceptionType.ACESSTOKEN_EXPIRED);
            new ObjectMapper().writeValue(response.getWriter(), customException);

        } catch (Exception e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            response.setStatus(SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");

            CustomException customException = new CustomException(MemberExceptionType.TOKEN_FORBIDDEN);
            new ObjectMapper().writeValue(response.getWriter(), customException);
        }

    }
}
