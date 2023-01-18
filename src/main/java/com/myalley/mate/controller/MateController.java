package com.myalley.mate.controller;

import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.mate.service.MateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/api/mates/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody MateUpdateRequest request) {
        mateService.update(id, request);
        return ResponseEntity.ok("메이트 모집글 수정이 완료되었습니다.");
    }

    @GetMapping("/mates/{id}")
    public ResponseEntity showMateDetail(@PathVariable Long id) {
        mateService.updateViewCount(id);
        return ResponseEntity.ok(mateService.findDetail(id));
    }

    @DeleteMapping("/api/mates/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        mateService.delete(id);
        return ResponseEntity.ok("전시회 정보가 삭제되었습니다.");
    }
}
