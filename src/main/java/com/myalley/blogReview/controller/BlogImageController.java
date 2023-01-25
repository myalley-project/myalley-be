package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/blogs/images")
@RequiredArgsConstructor
public class BlogImageController {
    private final BlogImageService blogImageService;
    private final BlogReviewService blogReviewService;

    //블로그 리뷰 내에 이미지 수정이 필요하여 삭제 요청을 보내는 경우 입니다
    @DeleteMapping("/{blog-id}/{image-id}")
    public ResponseEntity deleteBlogReviewImage(@PathVariable("blog-id") Long blogId,
                                                @PathVariable("image-id") Long imageId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview target = blogReviewService.findBlogReview(blogId);
        blogImageService.removeBlogImage(target,member,imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("이미지가 삭제 되었습니다.",headers,HttpStatus.OK);
    }

    //블로그 수정할 때는 이미지를 하나씩 등록 할 수 있도록 해줍니다
    @PostMapping("/{blog-id}")
    public ResponseEntity newPostBlogReviewImage(@PathVariable("blog-id") Long blogId,
                                                 @RequestPart(value = "image") MultipartFile image) throws Exception {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogReview target = blogReviewService.findBlogReview(blogId);
        blogImageService.createNewBlogImage(target,member,image);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("이미지가 등록 되었습니다",headers,HttpStatus.CREATED);
    }
}
