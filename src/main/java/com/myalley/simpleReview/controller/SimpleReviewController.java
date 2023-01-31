package com.myalley.simpleReview.controller;

import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.dto.SimpleRequestDto;
import com.myalley.simpleReview.mapper.SimpleReviewMapper;
import com.myalley.simpleReview.service.SimpleReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SimpleReviewController {
    private final SimpleReviewService simpleService;

    @PostMapping("/api/simple-reviews")
    public ResponseEntity postSimpleReview(@Valid @RequestBody SimpleRequestDto.PostSimpleDto simpleDto){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleReview newSimpleReviewInfo =
                SimpleReviewMapper.INSTANCE.postSimpleDtoToSimpleReview(simpleDto);
        simpleService.createSimpleReview(newSimpleReviewInfo,member, simpleDto.getExhibitionId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("한 줄 리뷰가 등록되었습니다",headers,HttpStatus.CREATED);
    }

    @PatchMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity patchSimpleReview(@PathVariable("simple-id") Long simpleId,
                                            @Valid @RequestBody SimpleRequestDto.PatchSimpleDto simpleDto){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleReview post = SimpleReviewMapper.INSTANCE.patchSimpleDtoToSimpleReview(simpleDto);
        simpleService.updateSimpleReview(simpleId,post, member);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("한 줄 리뷰가 수정되었습니다",headers,HttpStatus.OK);
    }

    @DeleteMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity deleteSimpleReview(@PathVariable("simple-id") Long simpleId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        simpleService.removeSimpleReview(simpleId,member);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("한 줄 리뷰가 삭제되었습니다",headers,HttpStatus.OK);
    }

    @GetMapping("/simple-reviews/exhibitions/{exhibition-id}")
    public ResponseEntity getExhibitionSimpleReviewList(@PathVariable("exhibition-id") Long exhibitionId,
                                                        @RequestParam(required = false, value = "page") Integer pageNo,
                                                        @RequestParam(required = false, value = "order") String orderType){
        Page<SimpleReview> simpleReviewPage = simpleService.retrieveExhibitionSimpleReviewList(exhibitionId,pageNo,orderType);
        return new ResponseEntity<>(SimpleReviewMapper.INSTANCE.listExhibitionSimpleReviewDto(simpleReviewPage),
                HttpStatus.OK);
    }

    @GetMapping("/api/simple-reviews/me")
    public ResponseEntity getUserSimpleReviewList(@RequestParam(required = false, value = "page") Integer pageNo){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<SimpleReview> simpleReviewPage = simpleService.retrieveUserSimpleReviewList(member, pageNo);
        return new ResponseEntity<>(SimpleReviewMapper.INSTANCE.listUserSimpleReviewDto(simpleReviewPage),
                HttpStatus.OK);
    }
}
