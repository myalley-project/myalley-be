package com.myalley.blogReview.controller;

import com.myalley.blogReview.service.BlogBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BlogBookmarkController {
    private final BlogBookmarkService blogBookmarkService;

    @PostMapping("/{blog-id}/{member-id}")
    public ResponseEntity postBlogBookmark(@PathVariable("blog-id") Long blogId,@PathVariable("member-id") Long memberId){
        blogBookmarkService.createBookmark(blogId, memberId);
        return new ResponseEntity<>("on", HttpStatus.CREATED);
    }

    @DeleteMapping("/{blog-id}/{member-id}")
    public ResponseEntity deleteBlogBookmark(@PathVariable("blog-id") Long blogId,@PathVariable("member-id") Long memberId){
        blogBookmarkService.deleteBookmark(blogId, memberId);
        return new ResponseEntity<>("off", HttpStatus.OK);
    }
}
