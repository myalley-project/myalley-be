package com.myalley.exhibition.controller;

import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.exhibition.service.ExhibitionBookmarkService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Log
public class ExhibitionBookmarkController {

    private final ExhibitionBookmarkService exhibitionBookmarkService;

    @PutMapping("/api/exhibitions/bookmarks/{exhibitionId}")
    public ResponseEntity switchBookmark(@PathVariable Long exhibitionId) {
        log.info("전시글 북마크 추가/삭제");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(exhibitionBookmarkService.switchExhibitionBookmark(member, exhibitionId));
    }

    @GetMapping("/api/exhibitions/bookmarks/me")
    public ResponseEntity findMyExhibitionBookmarks(@Positive @RequestParam("page") int page) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        log.info("본인의 전시글 북마크 목록 조회");

        Page<ExhibitionBasicResponse> exhibitionBookmarks = exhibitionBookmarkService.findBookmarksByMemberId(memberId, page);
        List<ExhibitionBasicResponse> exhibitions = exhibitionBookmarks
                .stream()
                .map(ExhibitionBasicResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions, exhibitionBookmarks),
                HttpStatus.OK);
    }
}
