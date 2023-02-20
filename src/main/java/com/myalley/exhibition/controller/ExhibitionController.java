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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Log
public class ExhibitionController {

    private final ExhibitionService exhibitionService;
    /**
     * 전시글 등록 요청
     * @param request 전시회 정보를 담은 request json body
     * @return "전시회 게시글 등록이 완료되었습니다." 메시지를 전달한다.
     * @author Hwadam
     * */
    @PostMapping("/api/exhibitions")
    public ResponseEntity save(@Valid @RequestBody ExhibitionRequest request) {
        log.info("전시회 정보 등록");
        exhibitionService.save(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("전시글 등록이 완료되었습니다.", headers, HttpStatus.OK);
    }

    /**
     * 전시글 수정 요청
     * @param updateRequest 전시회 정보 수정을 위한 정보를 담은 request body
     * @param id 수정하려는 전시글 정보에 해당하는 id를 파라미터로 보낸다.
     * @return 파라미터로 보낸 id에 해당하는 게시글이 존재하면 수정 요청 성공 시 수정 완료 문자열 메시지가 리턴된다.
     *         만약 파라미터로 보낸 id에 해당하는 게시글이 존재하지 않는다면 게시글이 존재하지 않는다는 Exception 메시지와 코드를 리턴한다.
     * @author Hwadam
     * */
    @PutMapping("/api/exhibitions/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody ExhibitionUpdateRequest updateRequest) {
        log.info("전시회 정보 수정");
        exhibitionService.update(updateRequest, id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("전시글 정보 수정이 완료되었습니다.", headers, HttpStatus.OK);
    }

    /**
     * 전시글 삭제 요청
     * @param id 삭제하려는 전시글 정보에 해당하는 id를 파라미터로 보낸다.
     * @return 파라미터로 보낸 id에 해당하는 게시글이 존재하면 수정 요청 성공 시 수정 완료 문자열 메시지가 리턴된다.
     *         만약 파라미터로 보낸 id에 해당하는 게시글이 존재하지 않는다면 게시글이 존재하지 않는다는 Exception 메시지와 코드를 리턴한다.
     * @author Hwadam
     * */
    @DeleteMapping("/api/exhibitions/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("전시회 정보 삭제");
        exhibitionService.delete(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("해당 전시회 게시글이 삭제되었습니다.", headers, HttpStatus.OK);
    }

    /**
     * 전시글 상세페이지 조회 요청
     * @param id 조회하려는 전시글 정보에 해당하는 id를 파라미터로 보낸다.
     * @return 파라미터로 보낸 id에 해당하는 게시글이 존재하면 전시글 정보를 담은 ExhibitionResponse 를 리턴하고 조회수 카운트를 증가시킨다.
     *         만약 파라미터로 보낸 id에 해당하는 게시글이 존재하지 않는다면 게시글이 존재하지 않는다는 Exception 메시지와 코드를 리턴한다.
     * @author Hwadam
     * */
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
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sort", required = true) String sortCriteria) {

        log.info("전시회 목록조회 상태/유형 필터 검색");

        Page<Exhibition> pageExhibitions;

        if (type.equals("전체") || type.isEmpty()) {
            pageExhibitions = exhibitionService.readPageAll(status, sortCriteria, page);
        } else {
            pageExhibitions = exhibitionService.findStatusAndType(status, type, page, sortCriteria);
        }
            List<ExhibitionBasicResponse> exhibitions = pageExhibitions
                    .stream()
                    .map(ExhibitionBasicResponse::of)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(
                    new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                    HttpStatus.OK);
        }


    //전시회 관람여부만 조회
    @GetMapping("/main/exhibitions")
    public ResponseEntity getExhibitionsAll(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status,
            @RequestParam(value = "sort", required = false) String sortCriteria) {

        log.info("전시회 목록조회 상태 필터 검색");
        Page<Exhibition> pageExhibitions = exhibitionService.readPageAll(status, sortCriteria, page);
        List<ExhibitionBasicResponse> exhibitions = pageExhibitions
                .stream()
                .map(ExhibitionBasicResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                HttpStatus.OK);
    }

}
