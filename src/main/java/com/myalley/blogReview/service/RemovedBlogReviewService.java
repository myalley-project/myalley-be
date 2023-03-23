package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.response.BlogListResponseDto;
import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemovedBlogReviewService {
    private final BlogReviewRepository reviewRepository;

    public BlogListResponseDto findRemovedBlogReviews(Member member,Integer pageNo){
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("modified_at").descending());
        else
            pageRequest = PageRequest.of(pageNo-1, 6, Sort.by("modified_at").descending());
        Page<BlogReview> myBlogReviewList = reviewRepository.selectRemovedAll(member, pageRequest);
        return BlogListResponseDto.blogOf(myBlogReviewList,"self");
    }

}
