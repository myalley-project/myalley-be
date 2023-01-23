package com.myalley.blogReview.controller;

import com.myalley.blogReview.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class BlogLikesController {
    private final LikesService likesService;

    //클릭 했을 때
    //-> 임시로 member-id도 pathVariable로 같이 받기
    //-> 추후에 변경할 예정
    @PutMapping("/{blog-id}/{member-id}")
    public ResponseEntity clickLikes(@PathVariable("blog-id") Long blogId,@PathVariable("member-id") Long memberId){
        Boolean result = likesService.findLikes(blogId,memberId);
        if(result)
            return new ResponseEntity<>("off", HttpStatus.OK);
        else return new ResponseEntity<>("on", HttpStatus.CREATED);
    }
}
