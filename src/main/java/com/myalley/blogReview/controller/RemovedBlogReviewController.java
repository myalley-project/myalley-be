package com.myalley.blogReview.controller;

import com.myalley.blogReview.service.RemovedBlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/blogs/trash",produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@Slf4j
public class RemovedBlogReviewController {
    private final RemovedBlogReviewService removedService;

    @GetMapping
    public ResponseEntity findRemovedBlogReviews(@RequestParam(value = "page",required = false) Integer pageNo){
        log.info("Request-Type : Get, Entity : BlogReview_List, Type : Removed");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(removedService.findRemovedBlogReviews(member, pageNo),HttpStatus.OK);
    }

    //1 - 6개까지의 블로그 삭제 가능!
    @DeleteMapping
    public ResponseEntity removeBlogReviewsPermanently(@RequestHeader(value="idList") List<Long> blogId){
        log.info("Request-Type : Delete_Permanently, Entity : BlogReview_List");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        removedService.removeBlogReviewsPermanently(blogId, member);
        return new ResponseEntity<>("여러개의 블로그 글이 영구 삭제되었습니다.", HttpStatus.OK);
    }
}
