package com.myalley.simpleReview.controller;

import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.dto.SimpleRequestDto;
import com.myalley.simpleReview.mapper.SimpleReviewMapper;
import com.myalley.simpleReview.service.SimpleReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponseEntity postSimpleReview(@Valid @RequestBody SimpleRequestDto.PostSimpleDto simpleDto){
        log.info("Request-Type : Post, Entity : SimpleReview");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SimpleReview newSimpleReviewInfo =
                SimpleReviewMapper.INSTANCE.postSimpleDtoToSimpleReview(simpleDto);
        simpleService.createSimpleReview(newSimpleReviewInfo,member, simpleDto.getExhibitionId());
        return new ResponseEntity<>("한 줄 리뷰가 등록되었습니다",HttpStatus.CREATED);
    }

    @PutMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity patchSimpleReview(@PathVariable("simple-id") Long simpleId,
                                            @Valid @RequestBody SimpleRequestDto.PatchSimpleDto simpleDto){
        log.info("Request-Type : Put, Entity : SimpleReview, Simple-ID : {}", simpleId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SimpleReview post = SimpleReviewMapper.INSTANCE.putSimpleDtoToSimpleReview(simpleDto);
        simpleService.updateSimpleReview(simpleId,post, member);
        return new ResponseEntity<>("한 줄 리뷰가 수정되었습니다",HttpStatus.OK);
    }

    @DeleteMapping("/api/simple-reviews/{simple-id}")
    public ResponseEntity deleteSimpleReview(@PathVariable("simple-id") Long simpleId){
        log.info("Request-Type : Delete, Entity : SimpleReview, Simple-ID : {}", simpleId);
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        simpleService.removeSimpleReview(simpleId,member);
        return new ResponseEntity<>("한 줄 리뷰가 삭제되었습니다",HttpStatus.OK);
    }

    @GetMapping("/simple-reviews/exhibitions/{exhibition-id}")
    public ResponseEntity getExhibitionSimpleReviewList(@PathVariable("exhibition-id") Long exhibitionId,
                                                        @RequestParam(required = false, value = "page") Integer pageNo,
                                                        @RequestParam(required = false, value = "order") String orderType){
        log.info("Request-Type : Get, Entity : SimpleReview_List, Type : Exhibition, Exhibition-ID : {}", exhibitionId);

        Page<SimpleReview> simpleReviewPage = simpleService.retrieveExhibitionSimpleReviewList(exhibitionId,pageNo,orderType);
        return new ResponseEntity<>(SimpleReviewMapper.INSTANCE.listExhibitionSimpleReviewDto(simpleReviewPage),
                HttpStatus.OK);
    }

    @GetMapping("/api/simple-reviews/me")
    public ResponseEntity getUserSimpleReviewList(@RequestParam(required = false, value = "page") Integer pageNo){
        log.info("Request-Type : Get, Entity : SimpleReview_List, Type : MyPage");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<SimpleReview> simpleReviewPage = simpleService.retrieveUserSimpleReviewList(member, pageNo);
        return new ResponseEntity<>(SimpleReviewMapper.INSTANCE.listUserSimpleReviewDto(simpleReviewPage),
                HttpStatus.OK);
    }
}
