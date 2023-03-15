package com.myalley.exhibition.controller;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@Log
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @PostMapping(value = "/api/exhibitions")
    public ResponseEntity save(@Valid @RequestBody ExhibitionRequest request) {
        log.info("전시회 정보 등록");
        return ResponseEntity.ok(exhibitionService.save(request));
    }

    @PutMapping("/api/exhibitions/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody ExhibitionUpdateRequest updateRequest) {
        log.info("전시회 정보 수정");
        return ResponseEntity.ok(exhibitionService.update(updateRequest, id));
    }

    @DeleteMapping("/api/exhibitions/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("전시회 정보 삭제");
        exhibitionService.delete(id);

        return new ResponseEntity<>("해당 전시회 게시글이 삭제되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/exhibitions/{id}")
    public ResponseEntity read(@PathVariable Long id, @RequestHeader("memberId") Long data) {
        Long memberId =  data;
        log.info("전시회 정보 상세페이지 조회");
        exhibitionService.updateViewCount(id);

        if (memberId == 0) {
            return ResponseEntity.ok(exhibitionService.findInfoGeneral(id));
        }
        return ResponseEntity.ok(exhibitionService.findInfoMember(id, memberId));
    }

    @GetMapping("/exhibitions")
    public ResponseEntity getExhibitions(@Positive @RequestParam int page,
                                                  @RequestParam(value = "status", required = false) String status,
                                                  @RequestParam(value = "type", required = false) String type,
                                                  @RequestParam(value = "sort", required = false) String sort,
                                                  @RequestParam(value = "title", required = false) String title) {

        Page<Exhibition> pagedExhibitions = exhibitionService.getExhibitionList(status, type, sort, title, page);
        List<ExhibitionBasicResponse> exhibitions = pagedExhibitions
                .stream()
                .map(ExhibitionBasicResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions , pagedExhibitions),HttpStatus.OK);
    }
}
