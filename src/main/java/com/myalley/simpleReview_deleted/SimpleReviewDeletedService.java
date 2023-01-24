package com.myalley.simpleReview_deleted;

import com.myalley.simpleReview.domain.SimpleReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SimpleReviewDeletedService {
    private final SimpleReviewDeletedRepository simpleDeletedRepository;

    public void createdSimpleDeleted(SimpleReview simpleReview){
        SimpleReviewDeleted newSimpleDeleted = SimpleReviewDeleted.builder()
                .viewDate(simpleReview.getViewDate())
                .rate(simpleReview.getRate())
                .time(simpleReview.getTime())
                .congestion(simpleReview.getCongestion())
                .content(simpleReview.getContent())
                .createdAt(simpleReview.getCreatedAt())
                .deletedAt(LocalDateTime.now())
                .memberId(simpleReview.getMember().getMemberId())
                .exhibitionId(simpleReview.getExhibition().getId())
                .build();
        simpleDeletedRepository.save(newSimpleDeleted);
    }
}
