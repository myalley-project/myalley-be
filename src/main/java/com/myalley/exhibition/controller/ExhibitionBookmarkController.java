package com.myalley.exhibition.controller;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.domain.ExhibitionBookmark;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.exhibition.service.BookmarkService;
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
public class ExhibitionBookmarkController {

    private final BookmarkService bookmarkService;

    @PutMapping("/api/exhibitions/bookmarks/{id}")
    public ResponseEntity addBookmark(@PathVariable Long id) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        return ResponseEntity.ok(bookmarkService.addNewBookmark(memberId, id));
    }

    @GetMapping("/api/exhibition-bookmarks/me")
    public ResponseEntity getExhibitionsAll(@Positive @RequestParam("page") int page) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        int size = 8;
        Page<ExhibitionBookmark> exhibitionBookmarks = bookmarkService.findBookmarkedExhibitions(memberId, page, size);
        List<ExhibitionBasicResponse> exhibitions = exhibitionBookmarks
                .stream()
                .map(ExhibitionBasicResponse::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions, exhibitionBookmarks),
                HttpStatus.OK);
    }


}
