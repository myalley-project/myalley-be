package com.myalley.mate.controller;

import com.myalley.mate.dto.response.MyMateBookmarkResponse;
import com.myalley.mate.dto.response.MatePageResponses;
import com.myalley.mate.service.MateBookmarkService;
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

@Log
@RestController
@RequiredArgsConstructor
@RequestMapping
public class MateBookmarkController {
    private final MateBookmarkService bookmarkService;

    @PutMapping("/api/mates/bookmarks/{id}")
    public ResponseEntity switchMateBookmark(@PathVariable Long id) {
        log.info("메이트글 북마크 추가/삭제");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(bookmarkService.switchMateBookmark(member, id));
    }

    @GetMapping("/api/mates/bookmarks/me")
    public ResponseEntity findMyBookmarkedMates(@Positive @RequestParam("page") int page) {

        log.info("본인의 메이트글 북마크 목록 조회");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        Page<MyMateBookmarkResponse> mateBookmarks = bookmarkService.findBookmarkedMatesByMemberId(memberId, page);
        List<MyMateBookmarkResponse> responses = mateBookmarks
                .stream()
                .map(MyMateBookmarkResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponses<>(responses, mateBookmarks), HttpStatus.OK);
    }
}
