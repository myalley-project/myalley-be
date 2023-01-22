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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // basic authentication
        http.httpBasic().disable(); // basic authentication filter 비활성화
        http.rememberMe().disable();

        http.cors();//.configurationSource(request -> {
//            var cors = new CorsConfiguration();
//            cors.setAllowedOrigins(List.of("*"));
//            cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
//            cors.setAllowedHeaders(List.of("*"));
//            cors.setAllowCredentials(true);
//            return cors;
//        });보류

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
                // /와 /home은 모두에게 허용
                .antMatchers( "/home", "/signup","/refresh","/blogs/**","/exhibitions/**","mates/**","/logout").permitAll()//"/login"
                // hello 페이지는 USER 롤을 가진 유저에게만 허용
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").hasAnyRole("USER","ADMIN")

//                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                .anyRequest().authenticated();

        // logout
//        http.csrf().disable().logout()
//                .logoutUrl("/logout")
//                .invalidateHttpSession(true);
//               // .deleteCookies(JwtProperties.COOKIE_NAME);
    }

    @Override
    public void configure(WebSecurity web) {
        // 정적 리소스 spring security 대상에서 제외 static 폴더아래
//        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        web.ignoring().antMatchers(  "/signup","/refresh","/blogs/**","/exhibitions/**","mates/**");
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
