package com.myalley.member;

import com.myalley.member.WithMockCustomUser;
import com.myalley.member.domain.Member;
import com.myalley.member.options.Gender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDate;
import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final SecurityContext securityContext= SecurityContextHolder.createEmptyContext();

        final UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(Member.builder()
                .email("test@naver.com")
                .password("Test1234!")
                .nickname("test")
                .birth(LocalDate.parse("2000-01-01"))
                .gender(Gender.valueOf("W"))
                .build(),null,  Arrays.asList(new SimpleGrantedAuthority(annotation.role())));

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
