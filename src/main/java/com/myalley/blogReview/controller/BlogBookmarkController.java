package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.service.BlogBookmarkService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs/bookmarks")
@RequiredArgsConstructor
@Slf4j
public class BlogBookmarkController {
    private final BlogBookmarkService blogBookmarkService;
    private final BlogReviewService blogReviewService;

    @PutMapping("/{blog-id}")
    public ResponseEntity clickBlogBookmark(@PathVariable("blog-id") Long blogId){
        log.info("Request-Type : Put, Entity : BlogBookmark, Blog-ID : {}", blogId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        if(blogBookmarkService.findBookmark(blogReview, member))
            return new ResponseEntity<>("on", HttpStatus.OK);
        else return new ResponseEntity<>("off", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity getMyBlogBookmark(@RequestParam(required = false, value = "page") Integer pageNo){
        log.info("Request-Type : Get, Entity : BlogBookmark_List, Type : MyPage");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(blogBookmarkService.retrieveMyBlogBookmarks(member,pageNo), HttpStatus.OK);
    }
}
