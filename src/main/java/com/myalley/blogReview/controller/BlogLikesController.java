package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogLikesService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class BlogLikesController {
    private final BlogLikesService blogLikesService;
    private final BlogReviewService blogReviewService;

    @PostMapping("/blogs/{blog-id}")
    public ResponseEntity postLikes(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        blogLikesService.createLikes(blogReview,member);
        return new ResponseEntity<>("on",HttpStatus.CREATED);
    }

    @DeleteMapping("/blogs/{blog-id}")
    public ResponseEntity deleteLikes(@PathVariable("blog-id") Long blogId) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        blogLikesService.removeLikes(blogReview, member);
        return new ResponseEntity<>("off", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity getMyBlogLikes(@RequestParam(required = false, value = "page") Integer pageNo){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<BlogLikes> likesPage = blogLikesService.retrieveMyBlogLikes(member,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableLikesToMyBlogLikesDto(likesPage),HttpStatus.OK);
    }
}
