package com.myalley.mate.controller;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.*;
import com.myalley.mate.service.MateService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    @PostMapping("/api/mates")
    public ResponseEntity save(@Valid @RequestBody MateRequest mateRequest) {
        log.info("메이트 모집글 등록");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.save(mateRequest, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("메이트 모집글 등록이 완료되었습니다.", headers, HttpStatus.OK);
    }

    @PutMapping("/api/mates/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody MateUpdateRequest request) {
        log.info("메이트 모집글 수정");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.update(id, request, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("메이트 모집글 수정이 완료되었습니다.", headers, HttpStatus.OK);
    }

    //메이트글 상세페이지 조회 (회원/비회원)
    @GetMapping("/mates/{id}")
    public ResponseEntity showMateDetail(@PathVariable Long id, @RequestHeader("memberId") Long data) {
        log.info("메이트 모집글 상세페이지 조회");

        if (data == null) {
            throw new CustomException(MateExceptionType.MEMBER_ID_IS_MANDATORY);
        }

        Long memberId =  data;
        mateService.updateViewCount(id);

        if (memberId == 0) {
            return ResponseEntity.ok(mateService.findDetail(id));
        }
        return ResponseEntity.ok(mateService.findDetail(id, memberId));
    }

    @DeleteMapping("/api/mates/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("메이트 모집글 삭제");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.delete(id, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("메이트 모집글 삭제가 완료되었습니다.", headers, HttpStatus.OK);
    }

    //메이트글 모집완료 여부 목록 조회
    @GetMapping("/mates")
    public ResponseEntity findMateAll(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status) {

        log.info("메이트 모집글 상태 필터 목록 조회 ");

        Page<Mate> mates;

        if (status.equals("전체")) {
            mates = mateService.findListsAll(page);
        } else if (status.equals("모집 중") || status.equals("모집 완료")) {
            mates = mateService.findListsByStatus(status, page);
        } else {
            throw new CustomException(MateExceptionType.UNAUTHORIZED_MATE_SEARCH_KEYWORD);
        }

       List<MateSimpleResponse> response = mates
               .stream()
               .map(MateSimpleResponse::of)
               .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(response, mates),
                HttpStatus.OK);
    }

    //본인이 작성한 글 조회
    @GetMapping("/api/mates/me")
    public ResponseEntity getMatesAll(@Positive @RequestParam("page") int page) {
        log.info("본인이 작성한 메이트 모집글 목록 조회");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        Page<Mate> mate = mateService.findMyMates(memberId, page);
        List<MateMyResponse> responses = mate
                .stream()
                .map(MateMyResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(responses, mate), HttpStatus.OK);
    }

    //전시글 상세페이지에서 해당 전시회에 해당하는 메이트 모집글 목록 조회
    @GetMapping("/exhibitions/mates/{id}")
    public ResponseEntity getMatesByExhibition(@Positive @RequestParam("page") int page,
                                               @PathVariable Long id) {
        log.info("상세페이지 메이트 모집글 목록 조회");

        Page<Mate> mates = mateService.findExhibitionMates(id, page);
        List<MateExhibitionResponse> response = mates
                .stream()
                .map(MateExhibitionResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(response, mates), HttpStatus.OK);
    }


    //메이트글 서치바 (제목 or 내용 검색)
    @GetMapping("/mates/search")
    public ResponseEntity findInfoByTitle( @Positive @RequestParam int page,
                                           @RequestParam(value = "keyword", required = false) String keyword) {

        log.info("메이트 모집글 서치바 제목 검색");
        Page<Mate> pageMate = mateService.findTitle(keyword, page);
        List<MateSimpleResponse> mates = pageMate
                .stream()
                .map(MateSimpleResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(mates, pageMate), HttpStatus.OK);

    }
}
