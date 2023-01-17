package com.myalley.mate.controller;

import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.service.MateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    @PostMapping("/api/mates")
    public ResponseEntity save(@Valid @RequestBody MateRequest mateRequest) {
        mateService.save(mateRequest);
        return ResponseEntity.ok("메이트 모집글 등록이 완료되었습니다.");
    }
}
