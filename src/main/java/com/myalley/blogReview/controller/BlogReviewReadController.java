package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogReviewReadController {
    private final BlogReviewService reviewService;
    //1. 상세
    @GetMapping("/{blog-id}")
    public ResponseEntity readBlogReviewDetail(@PathVariable("blog-id") Long blogId){
        BlogReview review = reviewService.retrieveBlogReview(blogId);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.blogToDetailBlogDto(review),HttpStatus.OK);
    }
    //2. 목록
    @GetMapping
    public ResponseEntity readBlogReviews(){
        //BlogReview review = reviewService.retrieveBlogReview(blogId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
