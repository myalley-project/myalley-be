package com.myalley.blogReview.controller;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.blogReview.service.S3Service;
import com.myalley.blogReview_deleted.service.BlogReviewDeletedService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BlogReviewController {

    private final BlogReviewService blogReviewService;


    @PostMapping("/api/blogs")
    public ResponseEntity postBlogReview(@RequestPart(value = "blogInfo") BlogRequestDto.PostBlogDto blogRequestDto,
                                           @RequestPart(required = false) List<MultipartFile> images) throws Exception { //@RequestPart("images") MultipartFile[] files,
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview target = BlogReviewMapper.INSTANCE.postBlogDtoToBlogReview(blogRequestDto);
        blogReviewService.createBlog(target, member,blogRequestDto.getExhibitionId(),images);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/blogs/{blog-id}")
    public ResponseEntity putBlogReview(@PathVariable("blog-id") Long blogId,
                                           @Valid @RequestBody BlogRequestDto.PutBlogDto blogRequestDto) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview target = BlogReviewMapper.INSTANCE.putBlogDtoToBlogReview(blogRequestDto);
        blogReviewService.updateBlogReview(target,blogId,member);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/api/blogs/{blog-id}")
    public ResponseEntity deleteBlogReview(@PathVariable("blog-id") Long blogId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blogReviewService.removeBlogReview(blogId,member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/blogs/me")
    public ResponseEntity getUserBlogReviewList(@RequestParam(value = "page",required = false) Integer pageNo){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<BlogReview> blogReviewPage = blogReviewService.retrieveMyBlogReviewList(member,pageNo);
        return new ResponseEntity<>(BlogReviewMapper.INSTANCE.pageableBlogToBlogListDto(blogReviewPage),HttpStatus.OK);
    }
}
