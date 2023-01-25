package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogBookmarkService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog-bookmarks")
@RequiredArgsConstructor
public class BlogBookmarkController {
    private final BlogBookmarkService blogBookmarkService;
    private final BlogReviewService blogReviewService;

    @PostMapping("/{blog-id}")
    public ResponseEntity postBlogBookmark(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        blogBookmarkService.createBookmark(blogReview, member);
        return new ResponseEntity<>("on", HttpStatus.CREATED);
    }

    @DeleteMapping("/{blog-id}")
    public ResponseEntity deleteBlogBookmark(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        blogBookmarkService.removeBookmark(blogReview, member);
        return new ResponseEntity<>("off", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity getMyBlogBookmark(@RequestParam(required = false, value = "page") Integer pageNo){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<BlogBookmark> bookmarkPage = blogBookmarkService.retrieveMyBlogBookmarks(member,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBookmarkToMyBlogBookmarkDto(bookmarkPage),
                HttpStatus.OK);
    }
}
