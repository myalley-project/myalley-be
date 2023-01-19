package com.myalley.blogReview.controller;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.blogReview.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping("/api/blogs/images")
@RequiredArgsConstructor
public class BlogImageController {
    private final BlogImageService blogImageService;
    private final BlogReviewService blogReviewService;
    private final S3Service s3Service;

    //블로그 리뷰 내에 이미지 수정이 필요하여 삭제 요청을 보내는 경우 입니다
    @DeleteMapping("/{blog-id}/{image-id}")
    public ResponseEntity deleteBlogReviewImage(@PathVariable("blog-id") Long blogId, @PathVariable("image-id") Long imageId){
        BlogImage foundImage = blogImageService.retrieveBlogImage(blogId,imageId);
        s3Service.deleteBlogImage(foundImage.getFileName());
        blogImageService.deleteBlogImage(foundImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //블로그 수정할 때는 이미지를 하나씩 등록 할 수 있도록 해줍니다 - 첫번째 생각한 방법
    @PostMapping("/{blog-id}")
    public ResponseEntity changeBlogReviewImage(@PathVariable("blog-id") Long blogId, MultipartFile file) throws Exception {
        //HashMap<String,String> imageInformationMap = s3Service.uploadBlogImages(files);
        //blogImageService.addBlogImages(imageInformationMap,blogId);
        BlogReview blogReview = blogReviewService.retrieveBlogReview(blogId);
        String[] information = s3Service.uploadBlogImage(file);
        blogImageService.addBlogImage(information[0],information[1],blogReview);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}