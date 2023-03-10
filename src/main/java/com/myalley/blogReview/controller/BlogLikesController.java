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
@RequestMapping("/api/blogs/likes")
@RequiredArgsConstructor
public class BlogLikesController {
    private final BlogLikesService blogLikesService;
    private final BlogReviewService blogReviewService;

    @PutMapping("/{blog-id}")
    public ResponseEntity clickLikes(@PathVariable("blog-id") Long blogId){
        //좋아요 클릭 - 블로그 id 포함. 멤버 정보가 필요할까?
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        if(blogLikesService.findLikes(blogReview,member))
            return new ResponseEntity<>("on", HttpStatus.OK);
        else return new ResponseEntity<>("off", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity getMyBlogLikes(@RequestParam(required = false, value = "page") Integer pageNo){
        //내 좋아요 목록 조회 - 멤버 정보가 필요할까?
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<BlogLikes> likesPage = blogLikesService.retrieveMyBlogLikes(member,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableLikesToMyBlogLikesDto(likesPage),HttpStatus.OK);
    }
}
