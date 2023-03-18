package com.myalley.member.controller;

import com.myalley.exception.CustomException;
import com.myalley.exception.MemberExceptionType;
import com.myalley.member.service.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LogInController {
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> createRefreshToken(@RequestBody HashMap<String, String> req) {

        String refreshToken = req.get("refreshToken");

        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new CustomException(MemberExceptionType.TOKEN_FORBIDDEN);
        }

        refreshToken = refreshToken.substring("Bearer ".length());
        Map<String, String> tokens = refreshService.createToken(refreshToken);

        return new ResponseEntity(tokens, HttpStatus.ACCEPTED);
    }


//    @PostMapping("/good")
//    public ResponseEntity<Map<String, String>> good(@RequestBody HashMap<String, String> req) {
//
//        String refreshToken = req.get("refreshToken");
//        return new ResponseEntity(refreshService.good(refreshToken),HttpStatus.ACCEPTED);
//
//    }
}
