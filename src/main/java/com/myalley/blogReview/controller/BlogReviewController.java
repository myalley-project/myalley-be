package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.DetailBlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponseEntity postBlogReview(@Valid @RequestPart(value = "blogInfo") BlogRequestDto.PostBlogDto blogRequestDto,
                                         @RequestPart(value = "images",required = false) List<MultipartFile> images,
                                         @RequestPart(value = "exhibitionId")Long exhibitionId) throws Exception {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Post, Entity : BlogReview, Member-ID : {}", member.getMemberId());

        BlogReview target = BlogReviewMapper.INSTANCE.postBlogDtoToBlogReview(blogRequestDto);
        blogReviewService.createBlog(target, member,exhibitionId,images);
        return new ResponseEntity<>("블로그 글이 등록되었습니다",HttpStatus.CREATED);
    }

    @PutMapping("/api/blogs/{blog-id}")
    public ResponseEntity putBlogReview(@PathVariable("blog-id") Long blogId,
                                           @Valid @RequestBody BlogRequestDto.PutBlogDto blogRequestDto) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Put, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, member.getMemberId());

        BlogReview target = BlogReviewMapper.INSTANCE.putBlogDtoToBlogReview(blogRequestDto);
        blogReviewService.updateBlogReview(target,blogId,member);
        return new ResponseEntity("블로그 글이 수정되었습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/api/blogs/{blog-id}")
    public ResponseEntity deleteBlogReview(@PathVariable("blog-id") Long blogId){
        //블로그 삭제 - 블로그 id 포함, 멤버 정보를 포함할까?
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Request-Type : Delete, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, member.getMemberId());

        blogReviewService.removeBlogReview(blogId,member);
        return new ResponseEntity<>("블로그 글이 삭제되었습니다.",HttpStatus.OK);
    }

    @GetMapping("/blogs/{blog-id}")
    public ResponseEntity getBlogReviewDetail(@PathVariable("blog-id") Long blogId,
                                              @RequestHeader(value = "memberId", required = false) Long memberId){
        //블로그 상세 정보 조회 - 블로그 id 포함
        log.info("Request-Type : Get, Entity : BlogReview, Blog-ID : {}, Member-ID : {}", blogId, memberId);
        DetailBlogReview review = blogReviewService.retrieveBlogReview(blogId, memberId);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.blogToDetailBlogDto(review),HttpStatus.OK);
    }

    @GetMapping("/blogs")
    public ResponseEntity getBlogReviews(@RequestParam(required = false, value = "page") Integer pageNo,
                                         @RequestParam(required = false, value = "order") String orderType){
        //블로그 목록 조회
        log.info("Request-Type : Get, Entity : BlogReview_List");
        Page<BlogReview> blogReviewPage = blogReviewService.retrieveBlogReviewList(pageNo,orderType);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBlogToBlogListDto(blogReviewPage),HttpStatus.OK);
    }

    @GetMapping("/blogs/search")
    public ResponseEntity getBlogReviewsWithSearch(@RequestParam(value = "title") String title,
                                         @RequestParam(required = false, value = "page") Integer pageNo){
        //블로그 목록 조회 - 검색어 포함
        log.info("Request-Type : Get, Entity : BlogReview_List, Search : {}", title);
        Page<BlogReview> blogReviewPage = blogReviewService.searchBlogReviewList(title,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBlogToBlogListDto(blogReviewPage),HttpStatus.OK);
    }

    @GetMapping("/api/blogs/me")
    public ResponseEntity getUserBlogReviewList(@RequestParam(value = "page",required = false) Integer pageNo){
        //내 블로그 목록 조회 - 멤버 정보 필요할까?
        log.info("Request-Type : Get, Entity : BlogReview_List");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<BlogReview> blogReviewPage = blogReviewService.retrieveMyBlogReviewList(member,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBlogToUserBlogListDto(blogReviewPage),HttpStatus.OK);
    }

    @GetMapping("/blogs/exhibitions/{exhibition-id}")
    public ResponseEntity getExhibitionBlogReviewList(@PathVariable("exhibition-id") Long exhibitionId,
                                                      @RequestParam(value = "page",required = false) Integer pageNo,
                                                      @RequestParam(value = "order",required = false) String orderType){
        //전시회의 블로그 목록 조회 - 전시회 id
        log.info("Request-Type : Get, Entity : BlogReview_List, Exhibition-ID : {}", exhibitionId);
        Page<BlogReview> blogReviewPage =
                blogReviewService.retrieveExhibitionBlogReviewList(exhibitionId,pageNo,orderType);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBlogToBlogListDto(blogReviewPage),HttpStatus.OK);
    }
}
