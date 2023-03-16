package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value="/api/blogs/images", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@Slf4j
public class BlogImageController {
    private final BlogImageService blogImageService;
    private final BlogReviewService blogReviewService;

    @DeleteMapping("/{blog-id}/{image-id}")
    public ResponseEntity deleteBlogReviewImage(@PathVariable("blog-id") Long blogId,
                                                @PathVariable("image-id") Long imageId){
        log.info("Request-Type : Delete, Entity : BlogImage, Blog-ID : {}, Image-ID : {}", blogId, imageId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BlogReview target = blogReviewService.findBlogReview(blogId);
        blogImageService.removeBlogImage(target,member,imageId);
        return new ResponseEntity<>("이미지가 삭제 되었습니다.",HttpStatus.OK);
    }

    @PostMapping("/{blog-id}")
    public ResponseEntity newPostBlogReviewImage(@PathVariable("blog-id") Long blogId,
                                                 @RequestPart(value = "image") MultipartFile image) throws Exception {
        log.info("Request-Type : Post, Entity : BlogImage, Blog-ID : {}", blogId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BlogReview target = blogReviewService.findBlogReview(blogId);
        blogImageService.createNewBlogImage(target,member,image);
        return new ResponseEntity<>("이미지가 등록 되었습니다",HttpStatus.CREATED);
    }
}
