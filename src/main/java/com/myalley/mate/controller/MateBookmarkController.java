package com.myalley.mate.controller;

import com.myalley.mate.domain.MateBookmark;
import com.myalley.mate.dto.MateMyResponse;
import com.myalley.mate.dto.MatePageResponse;
import com.myalley.mate.service.MateBookmarkService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
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
public class MateBookmarkController {
    private final MateBookmarkService bookmarkService;

    //북마크 추가 및 삭제
    @PutMapping("/api/mates/bookmarks/{id}")
    public ResponseEntity addBookmark(@PathVariable Long id) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        return ResponseEntity.ok(bookmarkService.addNewBookmark(memberId, id));
    }

    //회원 본인의 북마크한 게시글 조회
    @GetMapping("/api/mate-bookmarks/me")
    public ResponseEntity getMatesAll(@Positive @RequestParam("page") int page) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        int size = 8;

        Page<MateBookmark> mateBookmarks = bookmarkService.findBookmarkedMate(memberId, page, size);
        List<MateMyResponse> responses = mateBookmarks
                .stream()
                .map(MateMyResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MatePageResponse<>(responses, mateBookmarks), HttpStatus.OK);
    }
}
