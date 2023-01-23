package com.myalley.mate.controller;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionMateListResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.domain.MateBookmark;
import com.myalley.mate.dto.*;
import com.myalley.mate.service.MateService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    @PostMapping("/api/mates")
    public ResponseEntity save(@Valid @RequestBody MateRequest mateRequest) {
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
        Long memberId =  data;
        mateService.updateViewCount(id);

        if (memberId == 0) {
            return ResponseEntity.ok(mateService.findDetail(id));
        }
        return ResponseEntity.ok(mateService.findDetail(id, memberId));
    }

    @DeleteMapping("/api/mates/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
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
            @RequestParam(value = "status", required = false) String status) {
        int size = 4;
        Page<Mate> mates = mateService.readPageAll(status, page, size);
       List<MateSimpleResponse> response = mates
               .stream()
               .map(MateSimpleResponse::of)
               .collect(Collectors.toList());
//        List<MateSimpleResponse> mateList = responsePage.getContent();

        return new ResponseEntity<>(
                new MatePageResponse<>(response, mates),
                HttpStatus.OK);
    }

    //본인이 작성한 글 조회
    @GetMapping("/api/mates/me")
    public ResponseEntity getMatesAll(@Positive @RequestParam("page") int page) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        int size = 8;

        Page<Mate> mate = mateService.findMyMates(memberId, page, size);
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
        int size = 4;

        Page<Mate> mates = mateService.findExhibitionMates(id, page, size);
        List<MateSimpleResponse> response = mates
                .stream()
                .map(MateSimpleResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(response, mates), HttpStatus.OK);
    }


    //메이트글 서치바 (제목 or 내용 검색)
    @GetMapping("/mates/search")
    public ResponseEntity findInfoByTitle( @Positive @RequestParam int page,
                                           @RequestParam(value = "keyword", required = false) String keyword) {
        int size = 8;
        Page<Mate> pageMate = mateService.findTitleOrContent(keyword, page, size);
        List<MateSimpleResponse> mates = pageMate
                .stream()
                .map(MateSimpleResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(mates, pageMate), HttpStatus.OK);

    }
}
