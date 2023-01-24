package com.myalley.blogReview.controller;

import com.myalley.blogReview.service.BlogBookmarkService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog-bookmarks")
@RequiredArgsConstructor
public class BlogBookmarkController {
    private final BlogBookmarkService blogBookmarkService;

    @PostMapping("/{blog-id}")
    public ResponseEntity postBlogBookmark(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blogBookmarkService.createBookmark(blogId, member);
        return new ResponseEntity<>("on", HttpStatus.CREATED);
    }

    @DeleteMapping("/{blog-id}")
    public ResponseEntity deleteBlogBookmark(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blogBookmarkService.deleteBookmark(blogId, member);
        return new ResponseEntity<>("off", HttpStatus.OK);
    }
}
