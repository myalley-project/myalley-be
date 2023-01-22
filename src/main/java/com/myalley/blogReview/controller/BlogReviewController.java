package com.myalley.blogReview.controller;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.mapper.BlogReviewMapper;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.blogReview.service.S3Service;
import com.myalley.blogReview_deleted.service.BlogReviewDeletedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogReviewController {

    private final BlogReviewService blogReviewService;
    private final BlogReviewDeletedService blogReviewDeletedService;
    private final BlogImageService blogImageService;
    private final S3Service s3Service;


    @PostMapping("{member-id}")
    public ResponseEntity createBlogReview(@PathVariable("member-id") Long memberId,@RequestPart(value = "blogInfo") BlogRequestDto.PostBlogDto blogRequestDto, @RequestPart(required = false) List<MultipartFile> images) throws Exception { //@RequestPart("images") MultipartFile[] files,
        BlogReview target = BlogReviewMapper.INSTANCE.postBlogDtoToBlogReview(blogRequestDto);
        BlogReview newBlog = blogReviewService.createBlog(target, memberId);
        HashMap<String,String> map = s3Service.uploadBlogImages(images);
        if(!map.isEmpty())
            blogImageService.addBlogImageList(map, newBlog);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{blog-id}/{member-id}")
    public ResponseEntity updateBlogReview(@PathVariable("blog-id") Long blogId, @PathVariable("member-id") Long memberId, @RequestBody BlogRequestDto.PutBlogDto blogRequestDto) {
        BlogReview target = BlogReviewMapper.INSTANCE.putBlogDtoToBlogReview(blogRequestDto);
        blogReviewService.updateBlogReview(target,blogId,memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{blog-id}/{member-id}")
    public ResponseEntity deleteBlogReview(@PathVariable("blog-id") Long blogId,@PathVariable("member-id") Long memberId){
        BlogReview target = blogReviewService.preVerifyBlogReview(blogId,memberId);
        blogReviewDeletedService.addDeletedBlogReview(target);
        List<String> fileNameList = blogImageService.deleteBlogAllImages(blogId);
        s3Service.deleteBlogAllImages(fileNameList);
        blogReviewService.deleteBlogReview(target);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
