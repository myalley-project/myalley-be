package com.myalley.member.config;

import com.myalley.member.domain.Member;
import com.myalley.member.jwt.JwtAuthenticationFilter;
import com.myalley.member.jwt.JwtAuthorizationFilter;
import com.myalley.member.repository.MemberRepository;
import com.myalley.member.repository.TokenRedisRepository;
import com.myalley.member.service.MemberService;
import com.myalley.member.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable();
        http.rememberMe().disable();

        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOriginPatterns(List.of("*"));
            cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            cors.setAllowCredentials(true);
            return cors;
        });

        // stateless
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // jwt filter
        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager(),redisService),
                UsernamePasswordAuthenticationFilter.class
                 ).addFilterBefore(
                new JwtAuthorizationFilter(memberRepository),
                BasicAuthenticationFilter.class
        );
        http.formLogin().disable();

        // authorization경로별 설정
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/home","/", "/signup","/refresh","/blogs/**","/exhibitions/**","/mates/**","/logout","/main/**","/api/ping").permitAll()//"/login"
                .antMatchers("/api/admin/**","/api/exhibitions/**").hasRole("ADMIN")
                .antMatchers("/api/**").hasAnyRole("USER","ADMIN")
//                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                .anyRequest().authenticated();

    }

    @Override
    public void configure(WebSecurity web) {

        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        //web.ignoring().antMatchers(  "/home","/","/signup","/refresh","/blogs/**","/mates/**","/main/**","/exhibitions/**","/api/ping");
    }

    /**
     * UserDetailsService 구현
     *
     * @return UserDetailsService
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return email -> {
            Member member = memberService.findByEmail(email);
            if (member == null) {
                throw new UsernameNotFoundException(email);
            }
            return member;
        };
    }
}
