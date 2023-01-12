package com.myalley.blogReview.controller;
import com.myalley.blogReview.domain.Blog;
import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.service.BlogImageService;
import com.myalley.blogReview.service.BlogService;
import com.myalley.blogReview.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final BlogImageService blogImageService;
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity createBlogReview(BlogRequestDto blogRequestDto) throws Exception { //@RequestPart("images") MultipartFile[] files,
        Long createdBlogId = blogService.uploadBlog(blogRequestDto,2L);
        HashMap<String,String> map = s3Service.uploadBlogImages(blogRequestDto.getImages());
        if(map.isEmpty())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        blogImageService.addBlogImages(map, createdBlogId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //수정

    //삭제 - 이미지 사용됨

    //조회 - 이미지 사용됨
}
