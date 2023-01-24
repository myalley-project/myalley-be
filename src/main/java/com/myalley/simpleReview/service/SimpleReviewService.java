package com.myalley.simpleReview.service;

import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.repository.SimpleReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleReviewService {
    private final SimpleReviewRepository simpleRepository;
    private final ExhibitionService exhibitionService;

    public void createSimpleReview(SimpleReview simpleReview, Member member, Long exhibitionId){
        Exhibition exhibition = exhibitionService.verifyExhibition(exhibitionId);
        simpleReview.setMember(member);
        simpleReview.setExhibition(exhibition);
        simpleRepository.save(simpleReview);
    }


    //Test
    public SimpleReview getSimpleReview(Long simpleId){
        return simpleRepository.findById(simpleId).orElseThrow(()->{
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND); //원래는 simple review도 따로 있어야함
        });
    }
}
