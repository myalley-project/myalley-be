package com.myalley.blogReview.controller;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogReviewService;
import com.myalley.blogReview.service.S3Service;
import com.myalley.blogReview_deleted.service.BlogReviewDeletedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //우선 테스트 용으로 pathVariable을 통해 member-id 받아옴
    @PostMapping("{member-id}")
    public ResponseEntity createBlogReview( @PathVariable("member-id") Long memberId,BlogRequestDto blogRequestDto) throws Exception { //@RequestPart("images") MultipartFile[] files,
        BlogReview newBlog = blogReviewService.createBlog(blogRequestDto, memberId);
        HashMap<String,String> map = s3Service.uploadBlogImages(blogRequestDto.getImages());
        if(map.isEmpty())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        blogImageService.addBlogImageList(map, newBlog);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //우선 테스트 용으로 pathVariable을 통해 member-id 받아옴
    @PutMapping("/{blog-id}/{member-id}")
    public ResponseEntity updateBlogReview(@PathVariable("blog-id") Long blogId, @PathVariable("member-id") Long memberId, BlogRequestDto blogRequestDto) {
        blogReviewService.updateBlogReview(blogRequestDto,blogId,memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    //우선 테스트 용으로 pathVariable을 통해 member-id 받아옴
    @DeleteMapping("/{blog-id}/{member-id}")
    public ResponseEntity deleteBlogReview(@PathVariable("blog-id") Long blogId,@PathVariable("member-id") Long memberId){
        //BlogReview target = blogReviewService.deleteBlogReview(blogId,memberId);
        BlogReview target = blogReviewService.preVerifyBlogReview(blogId,memberId);
        blogReviewDeletedService.addDeletedBlogReview(target);
        List<String> fileNameList = blogImageService.deleteBlogAllImages(blogId);
        s3Service.deleteBlogAllImages(fileNameList);
        blogReviewService.deleteBlogReview(target);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //요청을 성공적으로 수행하였지만 Body에 들어가는 내용은 존재하지 않음
    }
}
