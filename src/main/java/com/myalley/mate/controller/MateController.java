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
@RequestMapping(produces = "application/json; charset=utf8")
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    @PostMapping("/api/mates")
    public ResponseEntity create(@Valid @RequestBody MateRequest request) {
        log.info("메이트 모집글 등록");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        return ResponseEntity.ok(mateService.save(request, memberId));
    }

    @PutMapping("/api/mates/{mateId}")
    public ResponseEntity update(@PathVariable Long mateId,
                                 @Valid @RequestBody MateUpdateRequest request) {
        log.info("메이트 모집글 수정");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        return ResponseEntity.ok(mateService.update(mateId, request, memberId));
    }

    //메이트글 상세페이지 조회 (회원/비회원)
    @GetMapping("/mates/{mateId}")
    public ResponseEntity getMateDetail(@PathVariable Long mateId, @RequestHeader("memberId") Long memberId) {
        log.info("메이트 모집글 상세페이지 조회");

        if (memberId == null) {
            throw new CustomException(MateExceptionType.MEMBER_ID_IS_MANDATORY);
        }

        mateService.updateViewCount(mateId);

        if (memberId == 0) {
            return ResponseEntity.ok(mateService.findDetail(mateId));
        }
        return ResponseEntity.ok(mateService.findDetail(mateId, memberId));
    }

    @DeleteMapping("/api/mates/{mateId}")
    public ResponseEntity delete(@PathVariable Long mateId) {
        log.info("메이트 모집글 삭제");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.delete(mateId, memberId);

        return new ResponseEntity<>("메이트 모집글 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    //메이트글 모집완료 여부 목록 조회
    @GetMapping("/mates")
    public ResponseEntity getMateListByStatus(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status) {

        log.info("메이트 모집글 상태 필터 목록 조회 ");

        Page<Mate> mates;

        if (status.equals("전체")) {
            mates = mateService.findListAll(page);
        } else if (status.equals("모집 중") || status.equals("모집 완료")) {
            mates = mateService.findListByStatus(status, page);
        } else {
            throw new CustomException(MateExceptionType.MATE_SORT_CRITERIA_ERROR);
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
    public ResponseEntity getMyMatePosts(@Positive @RequestParam("page") int page) {
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
    @GetMapping("/exhibitions/mates/{exhibitionId}")
    public ResponseEntity getMatesPostsByExhibition(@Positive @RequestParam("page") int page,
                                               @PathVariable Long exhibitionId) {
        log.info("상세페이지 메이트 모집글 목록 조회");

        Page<Mate> mates = mateService.findExhibitionMates(exhibitionId, page);
        List<MateExhibitionResponse> response = mates
                .stream()
                .map(MateExhibitionResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(response, mates), HttpStatus.OK);
    }


    //메이트글 서치바 (제목 or 내용 검색)
    @GetMapping("/mates/search")
    public ResponseEntity getMateListByTitle( @Positive @RequestParam int page,
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
