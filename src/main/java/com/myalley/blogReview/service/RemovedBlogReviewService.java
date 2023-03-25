package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.response.BlogListResponseDto;
import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
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

    public void removeBlogReviewPermanently(Long blogId, Member member) {
        BlogReview target = reviewRepository.selectRemovedById(blogId).orElseThrow(() -> {
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        if(target.getMember().getMemberId() != member.getMemberId()){
            throw new CustomException(BlogReviewExceptionType.BLOG_FORBIDDEN);
        }

        reviewRepository.removePermanently(target.getId());
    }
}
