package com.myalley.exhibition.controller;

import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionResponse;
import com.myalley.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    //전시글 등록
    @PostMapping("/api/exhibitions")
    public ResponseEntity save(@Valid @RequestBody ExhibitionRequest request) {
        exhibitionService.save(request);
        return ResponseEntity.ok("전시글 등록이 완료되었습니다.");
    }

    //전시글 수정
    @PatchMapping("/api/exhibitions/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody ExhibitionUpdateRequest updateRequest) {

        return ResponseEntity.ok("");
    }
}
