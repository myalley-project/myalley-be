package com.myalley.simpleReview.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.SimpleReviewExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.repository.SimpleReviewRepository;
import com.myalley.simpleReview_deleted.service.SimpleReviewDeletedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleReviewService {
    private final SimpleReviewRepository simpleRepository;
    private final ExhibitionService exhibitionService;
    private final SimpleReviewDeletedService simpleDeletedService;

    public void createSimpleReview(SimpleReview simpleReview, Member member, Long exhibitionId){
        Exhibition exhibition = exhibitionService.verifyExhibition(exhibitionId);
        simpleReview.setMember(member);
        simpleReview.setExhibition(exhibition);
        simpleRepository.save(simpleReview);
    }

    public void updateSimpleReview(Long simpleId, SimpleReview simpleReview, Member member){
        SimpleReview pre = verifySimpleReview(simpleId, member);
        pre.updateSimpleReview(simpleReview);
        simpleRepository.save(pre);
    }

    public void removeSimpleReview(Long simpleId, Member member){
        SimpleReview target = verifySimpleReview(simpleId, member);
        simpleDeletedService.createdSimpleDeleted(target);
        simpleRepository.delete(target);
    }

    public Page<SimpleReview> retrieveExhibitionSimpleReviewList(Long exhibitionId, Integer pageNo, String orderType){
        Exhibition exhibition = exhibitionService.verifyExhibition(exhibitionId);
        if(pageNo==null)
            pageNo=0;
        PageRequest pageRequest;
        if(orderType != null && orderType.equals("StarScore")) {
            pageRequest = PageRequest.of(pageNo, 10, Sort.by("rate").descending()
                    .and(Sort.by("id").descending()));
        }else if(orderType == null || orderType.equals("Recent"))
            pageRequest = PageRequest.of(pageNo,10, Sort.by("id").descending());
        else
            throw new CustomException(SimpleReviewExceptionType.SIMPLE_BAD_REQUEST);
        return simpleRepository.findAllByExhibition(exhibition,pageRequest);
    }

    public Page<SimpleReview> retrieveUserSimpleReviewList(Member member, Integer pageNo){
        if(pageNo==null)
            pageNo=0;
        PageRequest pageRequest = PageRequest.of(pageNo,5, Sort.by("id").descending());
        return simpleRepository.findAllByMember(member,pageRequest);
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
