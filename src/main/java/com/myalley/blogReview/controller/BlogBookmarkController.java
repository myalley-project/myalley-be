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
@RequestMapping("/api/blogs/bookmarks")
@RequiredArgsConstructor
public class BlogBookmarkController {
    private final BlogBookmarkService blogBookmarkService;
    private final BlogReviewService blogReviewService;

    @PutMapping("/{blog-id}")
    public ResponseEntity clickBlogBookmark(@PathVariable("blog-id") Long blogId){
        //북마크 클릭 - 블로그 id 포함. 멤버 정보가 필요할까?
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("** member id: "+member.getMemberId());
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        if(blogBookmarkService.findBookmark(blogReview, member))
            return new ResponseEntity<>("on", HttpStatus.OK);
        else return new ResponseEntity<>("off", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity getMyBlogBookmark(@RequestParam(required = false, value = "page") Integer pageNo){
        //내 북마크 목록 조회 - 멤버 정보가 필요할까?
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<BlogBookmark> bookmarkPage = blogBookmarkService.retrieveMyBlogBookmarks(member,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBookmarkToMyBlogBookmarkDto(bookmarkPage),
                HttpStatus.OK);
    }
}
