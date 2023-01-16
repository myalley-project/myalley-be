package com.myalley.member.controller;

import com.myalley.member.jwt.JwtUtils;
import com.myalley.member.repository.TokenRedisRepository;
import com.myalley.member.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final RedisService redisService;
    @PostMapping("/api/me/logout")
    public ResponseEntity<Map<String, Integer>> logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        String authorizationHeader = request.getHeader("AUTHORIZATION");
        String accessToken = authorizationHeader.substring("Bearer ".length());

        redisService.delete(JwtUtils.getEmail(accessToken));
        HashMap<String,Integer> result=new HashMap<>();
        result.put("resultCode",200);

        return new ResponseEntity(result, HttpStatus.ACCEPTED);
    }
}
