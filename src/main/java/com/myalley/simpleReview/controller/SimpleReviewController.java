package com.myalley.simpleReview.controller;

import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.dto.SimpleRequestDto;
import com.myalley.simpleReview.mapper.SimpleReviewMapper;
import com.myalley.simpleReview.service.SimpleReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SimpleReviewController {
    private final SimpleReviewService simpleService;

    @PostMapping("/api/simple-reviews")
    public ResponseEntity postSimpleReview(@Valid @RequestBody SimpleRequestDto.PostSimpleDto simpleDto){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleReview newSimpleReview = SimpleReviewMapper.INSTANCE.postSimpleDtoToSimpleReview(simpleDto);
        simpleService.createSimpleReview(newSimpleReview,member, simpleDto.getExhibitionId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity patchSimpleReview(@PathVariable("simple-id") Long simpleId,
                                            @RequestBody SimpleRequestDto.PatchSimpleDto simpleDto){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleReview post = SimpleReviewMapper.INSTANCE.patchSimpleDtoToSimpleReview(simpleDto);
        simpleService.updateSimpleReview(post, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity deleteSimpleReview(){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/simple-reviews/{exhibition-id}")
    public ResponseEntity getExhibitionSimpleReviewList(@PathVariable("exhibition-id") Long exhibitionId){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/api/simple-reviews/me")
    public ResponseEntity getUserSimpleReviewList(@PathParam("page") int pageNo){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Test
    @GetMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity getSimpleReview(@PathVariable("simple-id") Long simpleId){
        //Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleReview simpleReview = simpleService.getSimpleReview(simpleId);
        return new ResponseEntity<>(SimpleReviewMapper.INSTANCE.simpleReviewToGetSimpleResponseDto(simpleReview),
                HttpStatus.OK);
    }
}
