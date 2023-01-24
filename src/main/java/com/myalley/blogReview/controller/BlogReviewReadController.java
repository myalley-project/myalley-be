package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BlogReviewReadController {
    private final BlogReviewService reviewService;
    //1. 상세
    @GetMapping("/blogs/{blog-id}")
    public ResponseEntity getBlogReviewDetail(@PathVariable("blog-id") Long blogId){
        BlogReview review = reviewService.retrieveBlogReview(blogId);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.blogToDetailBlogDto(review),HttpStatus.OK);
    }
    //2. 목록
    @GetMapping("/blogs")
    public ResponseEntity getBlogReviews(@RequestParam(value = "page") int pageNo,
                                          @RequestParam(required = false, value = "order") String orderType){
        Page<BlogReview> blogReviewPage = reviewService.retrieveBlogReviewList(pageNo,orderType);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBlogToBlogListDto(blogReviewPage),HttpStatus.OK);
    }
}
