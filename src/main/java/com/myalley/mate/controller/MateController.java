package com.myalley.mate.controller;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.mate.dto.request.MateRequest;
import com.myalley.mate.dto.request.MateUpdateRequest;
import com.myalley.mate.dto.response.MateMyResponse;
import com.myalley.mate.dto.response.MatePageResponses;
import com.myalley.mate.dto.response.MateSimpleResponse;
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
    public ResponseEntity createMate(@Valid @RequestBody MateRequest request) {
        log.info("메이트 모집글 등록");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        mateService.createMate(request, memberId);
        return new ResponseEntity<>("메이트글 등록이 완료되었습니다.", HttpStatus.OK);
    }

    @PutMapping("/api/mates/{mateId}")
    public ResponseEntity updateMate(@PathVariable Long mateId,
                                 @Valid @RequestBody MateUpdateRequest request) {
        log.info("메이트 모집글 수정");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        mateService.updateMate(mateId, request, memberId);
        return new ResponseEntity<>("메이트글 수정이 완료되었습니다.", HttpStatus.OK);
    }

    //메이트글 상세페이지 조회 (회원/비회원)
    @GetMapping("/mates/{mateId}")
    public ResponseEntity findByMateId(@PathVariable Long mateId, @RequestHeader("memberId") Long memberId) {
        log.info("메이트 모집글 상세페이지 조회");

        if (memberId == null) {
            throw new CustomException(MateExceptionType.MEMBER_ID_IS_MANDATORY);
        }
        mateService.updateViewCount(mateId);
        return ResponseEntity.ok(mateService.findByMateIdAndMemberId(mateId, memberId));
    }

    @DeleteMapping("/api/mates/{mateId}")
    public ResponseEntity removeMate(@PathVariable Long mateId) {
        log.info("메이트 모집글 삭제");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.removeByMateIdAndMemberId(mateId, memberId);

        return new ResponseEntity<>("메이트 모집글 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/mates")
    public ResponseEntity findPagedMates( @Positive @RequestParam int page,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "title", required = false) String title) {

        log.info("메이트 모집글 목록 조회");

        Page<MateSimpleResponse> mates = mateService.findMatesByStatusAndTitle(status, title, page);
        List<MateSimpleResponse> response = mates
                .stream()
                .map(MateSimpleResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponses<>(response, mates),
                HttpStatus.OK);

    }

    //본인이 작성한 글 조회
    @GetMapping("/api/mates/me")
    public ResponseEntity findMyMates(@Positive @RequestParam("page") int page) {
        log.info("본인이 작성한 메이트 모집글 목록 조회");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        Page<MateMyResponse> mate = mateService.findMyMates(memberId, page);
        List<MateMyResponse> responses = mate
                .stream()
                .map(MateMyResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponses<>(responses, mate), HttpStatus.OK);
    }

    @GetMapping("/exhibitions/mates/{exhibitionId}")
    public ResponseEntity findMatesByExhibition(@Positive @RequestParam("page") int page,
                                               @PathVariable Long exhibitionId) {
        log.info("전시회 상세페이지 메이트 모집글 목록 조회");

        Page<MateSimpleResponse> mates = mateService.findMatesByExhibitionId(exhibitionId, page);
        List<MateSimpleResponse> response = mates
                .stream()
                .map(MateSimpleResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponses<>(response, mates), HttpStatus.OK);
    }
}
