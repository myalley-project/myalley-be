package com.myalley.blogReview.controller;

import com.myalley.blogReview.dto.request.BlogRequestDto;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@Slf4j
public class BlogReviewController {
    private final BlogReviewService blogReviewService;

    @PostMapping("/api/blogs")
    public ResponseEntity postBlogReview(@Valid @RequestPart(value = "blogInfo") BlogRequestDto blogRequestDto,
                                         @RequestPart(value = "images",required = false) List<MultipartFile> images,
                                         @RequestPart(value = "exhibitionId")Long exhibitionId) throws Exception {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Post, Entity : BlogReview, Member-ID : {}", member.getMemberId());

        blogReviewService.createBlog(blogRequestDto, member,exhibitionId,images);
        return new ResponseEntity<>("블로그 글이 등록되었습니다",HttpStatus.CREATED);
    }

    @PutMapping("/api/blogs/{blog-id}")
    public ResponseEntity putBlogReview(@PathVariable("blog-id") Long blogId,
                                           @Valid @RequestBody BlogRequestDto blogRequestDto) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Put, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, member.getMemberId());

        blogReviewService.updateBlogReview(blogRequestDto,blogId,member);
        return new ResponseEntity("블로그 글이 수정되었습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/api/blogs/{blog-id}")
    public ResponseEntity deleteBlogReview(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Delete, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, member.getMemberId());

        blogReviewService.removeBlogReview(blogId,member);
        return new ResponseEntity<>("블로그 글이 삭제되었습니다.",HttpStatus.OK);
    }

    @GetMapping("/blogs/{blog-id}")
    public ResponseEntity getBlogReviewDetail(@PathVariable("blog-id") Long blogId,
                                              @RequestHeader(value = "memberId", required = false) Long memberId){
        log.info("Request-Type : Get, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, memberId);

        return new ResponseEntity<>(blogReviewService.retrieveBlogReview(blogId, memberId),HttpStatus.OK);
    }

    @GetMapping("/blogs")
    public ResponseEntity getBlogReviews(@RequestParam(required = false, value = "page") Integer pageNo,
                                         @RequestParam(required = false, value = "order") String orderType){
        log.info("Request-Type : Get, Entity : BlogReview_List");

        return new ResponseEntity<>(blogReviewService.retrieveBlogReviewList(pageNo,orderType),HttpStatus.OK);
    }

    @GetMapping("/blogs/search")
    public ResponseEntity getBlogReviewsWithSearch(@RequestParam(value = "title") String title,
                                         @RequestParam(required = false, value = "page") Integer pageNo){
        log.info("Request-Type : Get, Entity : BlogReview_List, Search : {}", title);

        return new ResponseEntity<>(blogReviewService.searchBlogReviewList(title,pageNo),HttpStatus.OK);
    }

    @GetMapping("/api/blogs/me")
    public ResponseEntity getUserBlogReviewList(@RequestParam(value = "page",required = false) Integer pageNo){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Get, Entity : BlogReview_List, Member-ID : {}", member.getMemberId());

        return new ResponseEntity<>(blogReviewService.retrieveMyBlogReviewList(member,pageNo),HttpStatus.OK);
    }

    @GetMapping("/blogs/exhibitions/{exhibition-id}")
    public ResponseEntity getExhibitionBlogReviewList(@PathVariable("exhibition-id") Long exhibitionId,
                                                      @RequestParam(value = "page",required = false) Integer pageNo,
                                                      @RequestParam(value = "order",required = false) String orderType){
        log.info("Request-Type : Get, Entity : BlogReview_List, Exhibition-ID : {}", exhibitionId);

        return new ResponseEntity<>(blogReviewService.retrieveExhibitionBlogReviewList(exhibitionId,pageNo,orderType),
                HttpStatus.OK);
    }
}
