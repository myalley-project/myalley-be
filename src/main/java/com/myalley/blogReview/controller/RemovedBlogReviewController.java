package com.myalley.blogReview.controller;

import com.myalley.blogReview.service.RemovedBlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    //관리자 용 글 삭제 : LocalTime 기준으로 존재하는 글 삭제할 수 있는지 확인해보장
    //- 보관이 3일 이라면 수정일 기준 +3일이 되면 삭제가 되어야함
    //'삭제된 글 중에, 수정일 기준+3일이 오늘보다 작은 경우' 를 삭제하면 됨

    //DB에서 글 삭제 - 영구삭제 (1장씩, 전체 삭제)
    @DeleteMapping("/{blog-id}")
    public ResponseEntity removeBlogReviewPermanently(@PathVariable("blog-id") Long blogId){
        log.info("Request-Type : Delete_Permanently, Entity : BlogReview");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        removedService.removeBlogReviewPermanently(blogId, member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
