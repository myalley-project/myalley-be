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
    public ResponseEntity createBlogReview(@Valid @RequestPart(value = "blogInfo") BlogRequestDto blogRequestDto,
                                         @RequestPart(value = "images",required = false) List<MultipartFile> images,
                                         @RequestPart(value = "exhibitionId")Long exhibitionId) throws Exception {
        log.info("Request-Type : Post, Entity : BlogReview");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        blogReviewService.createBlog(blogRequestDto, member,exhibitionId,images);
        return new ResponseEntity<>("블로그 글이 등록되었습니다",HttpStatus.CREATED);
    }

    @PutMapping("/api/blogs/{blog-id}")
    public ResponseEntity updateBlogReview(@PathVariable("blog-id") Long blogId,
                                           @Valid @RequestBody BlogRequestDto blogRequestDto) {
        log.info("Request-Type : Put, Entity : BlogReview, Blog-ID : {}", blogId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        blogReviewService.updateBlogReview(blogRequestDto,blogId,member);
        return new ResponseEntity("블로그 글이 수정되었습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/api/blogs/{blog-id}")
    public ResponseEntity removeBlogReview(@PathVariable("blog-id") Long blogId){
        log.info("Request-Type : Delete, Entity : BlogReview, Blog-ID : {}", blogId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        blogReviewService.removeBlogReview(blogId,member);
        return new ResponseEntity<>("블로그 글이 삭제되었습니다.",HttpStatus.OK);
    }

    @GetMapping("/blogs/{blog-id}")
    public ResponseEntity findBlogReviewByBlogId(@PathVariable("blog-id") Long blogId,
                                              @RequestHeader(value = "memberId", required = false) Long memberId){
        log.info("Request-Type : Get, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, memberId);

        return new ResponseEntity<>(blogReviewService.findBlogReviewByBlogId(blogId, memberId),HttpStatus.OK);
    }

    @GetMapping("/blogs")
    public ResponseEntity findPagedBlogReviews(@RequestParam(required = false, value = "page") Integer pageNo,
                                         @RequestParam(required = false, value = "order") String orderType){
        log.info("Request-Type : Get, Entity : BlogReview_List, Type : Basic");

        return new ResponseEntity<>(blogReviewService.findPagedBlogReviews(pageNo,orderType),HttpStatus.OK);
    }

    @GetMapping("/blogs/search")
    public ResponseEntity findPagedBlogReviewsByTitle(@RequestParam(value = "title") String title,
                                         @RequestParam(required = false, value = "page") Integer pageNo){
        log.info("Request-Type : Get, Entity : BlogReview_List, Type : Search, Word : {}", title);

        return new ResponseEntity<>(blogReviewService.findPagedBlogReviewsByTitle(title,pageNo),HttpStatus.OK);
    }

    @GetMapping("/api/blogs/me")
    public ResponseEntity findMyBlogReviews(@RequestParam(value = "page",required = false) Integer pageNo){
        log.info("Request-Type : Get, Entity : BlogReview_List, Type : MyPage");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(blogReviewService.findMyBlogReviews(member,pageNo),HttpStatus.OK);
    }

    @GetMapping("/blogs/exhibitions/{exhibition-id}")
    public ResponseEntity findPagedBlogReviewsByExhibitionId(@PathVariable("exhibition-id") Long exhibitionId,
                                                      @RequestParam(value = "page",required = false) Integer pageNo,
                                                      @RequestParam(value = "order",required = false) String orderType){
        log.info("Request-Type : Get, Entity : BlogReview_List, Type : Exhibition, Exhibition-ID : {}", exhibitionId);

        return new ResponseEntity<>(blogReviewService.findPagedBlogReviewsByExhibitionId(exhibitionId,pageNo,orderType),
                HttpStatus.OK);
    }
}
