package com.myalley.simpleReview.service;

import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.exception.SimpleReviewExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.repository.SimpleReviewRepository;
import com.myalley.simpleReview_deleted.SimpleReviewDeleted;
import com.myalley.simpleReview_deleted.SimpleReviewDeletedRepository;
import com.myalley.simpleReview_deleted.SimpleReviewDeletedService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.build.Plugin;
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

    public void updateSimpleReview(SimpleReview simpleReview, Member member){
        SimpleReview pre = verifySimpleReview(simpleReview.getId(), member);
        pre.updateSimpleReview(simpleReview);
        simpleRepository.save(pre);
    }

    public void removeSimpleReview(Long simpleId, Member member){
        SimpleReview target = verifySimpleReview(simpleId, member);
        simpleDeletedService.createdSimpleDeleted(target);
        simpleRepository.delete(target);
    }

    public Page<SimpleReview> retrieveExhibitionSimpleReviewList(Long exhibitionId, int pageNo){
        Exhibition exhibition = exhibitionService.verifyExhibition(exhibitionId);
        PageRequest pageRequest = PageRequest.of(pageNo,10, Sort.by("id").descending());
        return simpleRepository.findAllByExhibition(exhibition,pageRequest);
    }


    //Test
    public SimpleReview getSimpleReview(Long simpleId){
        return simpleRepository.findById(simpleId).orElseThrow(()->{
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND); //원래는 simple review도 따로 있어야함
        });
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
