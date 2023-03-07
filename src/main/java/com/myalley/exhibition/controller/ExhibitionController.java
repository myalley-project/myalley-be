package com.myalley.exhibition.controller;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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


    //전시글 목록조회 서치바
    @GetMapping("/exhibitions/search")
    public ResponseEntity findInfoByTitle( @Positive @RequestParam int page,
                                           @RequestParam(value = "status", required = true) String status,
                                           @RequestParam(value = "title", required = false) String title) {
        int size = 8;

        log.info("전시회 목록조회 서치바 검색");

        Page<Exhibition> pageExhibitions = exhibitionService.findTitle(status, title, page, size);
        List<ExhibitionBasicResponse> exhibitions = pageExhibitions
                .stream()
                .map(ExhibitionBasicResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                HttpStatus.OK);

    }

    //전시회 상태와 유형 같이 검색
    @GetMapping("/exhibitions")
    public ResponseEntity getExhibitions(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "sort", required = true) String sortCriteria) {

        log.info("전시회 목록조회 상태/유형 필터 검색");

        Page<Exhibition> pageExhibitions;

        if (type.isEmpty() || sortCriteria.isEmpty() || status.isEmpty()) {
            throw new CustomException(ExhibitionExceptionType.EXHIBITION_SORT_CRITERIA_ERROR);
        } else if (type.equals("전체 전시")) {
            pageExhibitions = exhibitionService.findListsAll(status, sortCriteria, page);
        } else pageExhibitions = exhibitionService.findSListsByStatusAndType(status, type, page, sortCriteria);

            List<ExhibitionBasicResponse> exhibitions = pageExhibitions
                    .stream()
                    .map(ExhibitionBasicResponse::of)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(
                    new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                    HttpStatus.OK);
        }


    //전시회 관람여부만 조회 - 수정하면서 사용하지 않게 됨
    @GetMapping("/main/exhibitions")
    public ResponseEntity getExhibitionsAll(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status,
            @RequestParam(value = "sort", required = false) String sortCriteria) {

        log.info("전시회 목록조회 상태 필터 검색");
        Page<Exhibition> pageExhibitions = exhibitionService.findListsAll(status, sortCriteria, page);
        List<ExhibitionBasicResponse> exhibitions = pageExhibitions
                .stream()
                .map(ExhibitionBasicResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                HttpStatus.OK);
    }

}
