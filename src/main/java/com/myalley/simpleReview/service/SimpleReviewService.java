package com.myalley.simpleReview.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.SimpleReviewExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.dto.request.PostSimpleDto;
import com.myalley.simpleReview.dto.request.PutSimpleDto;
import com.myalley.simpleReview.dto.response.SimpleListResponseDto;
import com.myalley.simpleReview.repository.SimpleReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SimpleReviewService {
    private final SimpleReviewRepository simpleRepository;
    private final ExhibitionService exhibitionService;

    public void createSimpleReview(PostSimpleDto simpleReviewDto, Member member){
        Exhibition exhibition = exhibitionService.validateExistExhibition(simpleReviewDto.getExhibitionId());
        SimpleReview newSimpleReview = SimpleReview.builder()
                .viewDate(simpleReviewDto.getViewDate())
                .time(simpleReviewDto.getTime())
                .congestion(simpleReviewDto.getCongestion())
                .rate(simpleReviewDto.getRate())
                .content(simpleReviewDto.getContent())
                .member(member)
                .exhibition(exhibition)
                .build();
        simpleRepository.save(newSimpleReview);
    }

    @Transactional
    public void updateSimpleReview(Long simpleId, PutSimpleDto simpleReviewDto, Member member){
        SimpleReview pre = verifySimpleReview(simpleId, member);
        pre.updateSimpleReview(simpleReviewDto);
        simpleRepository.save(pre);
    }

    @Transactional
    public void removeSimpleReview(Long simpleId, Member member){
        SimpleReview target = verifySimpleReview(simpleId, member);
        simpleRepository.delete(target);
    }

    public SimpleListResponseDto findPagedSimpleReviewsByExhibitionId(Long exhibitionId, Integer pageNo, String orderType){
        Exhibition exhibition = exhibitionService.validateExistExhibition(exhibitionId);
        if(pageNo==null)
            pageNo=0;
        else
            pageNo--;
        PageRequest pageRequest;
        if(orderType != null && orderType.equals("StarScore")) {
            pageRequest = PageRequest.of(pageNo, 10, Sort.by("rate").descending()
                    .and(Sort.by("id").descending()));
        }else if(orderType == null || orderType.equals("Recent"))
            pageRequest = PageRequest.of(pageNo,10, Sort.by("id").descending());
        else
            throw new CustomException(SimpleReviewExceptionType.SIMPLE_BAD_REQUEST);
        return SimpleListResponseDto.of(simpleRepository.findAllByExhibition(exhibition,pageRequest), "member");
    }

    public SimpleListResponseDto findMySimpleReviews(Member member, Integer pageNo){
        if(pageNo==null)
            pageNo=0;
        else
            pageNo--;
        PageRequest pageRequest = PageRequest.of(pageNo,5, Sort.by("id").descending());
        return SimpleListResponseDto.of(simpleRepository.findAllByMember(member,pageRequest), "exhibition");
    }

    //존재하는지, 작성자 본인인지 확인
    private SimpleReview verifySimpleReview(Long simpleId, Member member) {
        SimpleReview simpleReview = simpleRepository.findById(simpleId).orElseThrow(() -> {
            throw new CustomException(SimpleReviewExceptionType.SIMPLE_NOT_FOUND);
        });
        if(simpleReview.getMember().getMemberId() != member.getMemberId())
            throw new CustomException(SimpleReviewExceptionType.SIMPLE_FORBIDDEN);
        return simpleReview;
    }
}
