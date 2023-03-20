package com.myalley.simpleReview.controller;

import com.myalley.member.domain.Member;
import com.myalley.simpleReview.dto.request.PostSimpleDto;
import com.myalley.simpleReview.dto.request.PutSimpleDto;
import com.myalley.simpleReview.service.SimpleReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@Slf4j
public class SimpleReviewController {
    private final SimpleReviewService simpleService;

    @PostMapping("/api/simple-reviews")
    public ResponseEntity createSimpleReview(@Valid @RequestBody PostSimpleDto simpleDto){
        log.info("Request-Type : Post, Entity : SimpleReview");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        simpleService.createSimpleReview(simpleDto, member);
        return new ResponseEntity<>("한 줄 리뷰가 등록되었습니다",HttpStatus.CREATED);
    }

    @PutMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity updateSimpleReview(@PathVariable("simple-id") Long simpleId,
                                            @Valid @RequestBody PutSimpleDto simpleDto){
        log.info("Request-Type : Put, Entity : SimpleReview, Simple-ID : {}", simpleId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        simpleService.updateSimpleReview(simpleId, simpleDto, member);
        return new ResponseEntity<>("한 줄 리뷰가 수정되었습니다",HttpStatus.OK);
    }

    @DeleteMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity removeSimpleReview(@PathVariable("simple-id") Long simpleId){
        log.info("Request-Type : Delete, Entity : SimpleReview, Simple-ID : {}", simpleId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        simpleService.removeSimpleReview(simpleId,member);
        return new ResponseEntity<>("한 줄 리뷰가 삭제되었습니다",HttpStatus.OK);
    }

    @GetMapping("/simple-reviews/exhibitions/{exhibition-id}")
    public ResponseEntity findPagedSimpleReviewsByExhibitionId(@PathVariable("exhibition-id") Long exhibitionId,
                                                        @RequestParam(required = false, value = "page") Integer pageNo,
                                                        @RequestParam(required = false, value = "order") String orderType){
        log.info("Request-Type : Get, Entity : SimpleReview_List, Type : Exhibition, Exhibition-ID : {}", exhibitionId);

        return new ResponseEntity<>(simpleService.findPagedSimpleReviewsByExhibitionId(exhibitionId,pageNo,orderType),
                HttpStatus.OK);
    }

    @GetMapping("/api/simple-reviews/me")
    public ResponseEntity findMySimpleReviews(@RequestParam(required = false, value = "page") Integer pageNo){
        log.info("Request-Type : Get, Entity : SimpleReview_List, Type : MyPage");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(simpleService.findMySimpleReviews(member, pageNo), HttpStatus.OK);
    }
}
