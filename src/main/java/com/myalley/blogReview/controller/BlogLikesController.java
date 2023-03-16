package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.service.BlogLikesService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs/likes")
@RequiredArgsConstructor
@Slf4j
public class BlogLikesController {
    private final BlogLikesService blogLikesService;
    private final BlogReviewService blogReviewService;

    @PutMapping("/{blog-id}")
    public ResponseEntity switchBlogLikes(@PathVariable("blog-id") Long blogId){
        log.info("Request-Type : Put, Entity : BlogLikes, Blog-ID : {}", blogId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BlogReview blogReview = blogReviewService.validateBlogReview(blogId);
        if(blogLikesService.switchBlogLikes(blogReview,member))
            return new ResponseEntity<>("on", HttpStatus.OK);
        else return new ResponseEntity<>("off", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity findMyLikedBlogReviews(@RequestParam(required = false, value = "page") Integer pageNo){
        log.info("Request-Type : Get, Entity : BlogLikes_List, Type : MyPage");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(blogLikesService.findMyLikedBlogReviews(member,pageNo),HttpStatus.OK);
    }
}
