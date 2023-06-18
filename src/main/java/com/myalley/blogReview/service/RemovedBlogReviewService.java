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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemovedBlogReviewService {
    private final BlogReviewRepository reviewRepository;
    private final BlogImageService imageService;

    public BlogListResponseDto findRemovedBlogReviews(Member member,Integer pageNo){
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("modified_at").descending());
        else
            pageRequest = PageRequest.of(pageNo-1, 6, Sort.by("modified_at").descending());
        Page<BlogReview> myBlogReviewList = reviewRepository.selectRemovedAll(member, pageRequest);
        return BlogListResponseDto.blogOf(myBlogReviewList,"self");
    }

    public void removeBlogReviewsPermanently(List<Long> blogId, Member member) {
        List<BlogReview> targetList = reviewRepository.selectRemovedByIdList(member.getMemberId(), blogId);
        if(CollectionUtils.isEmpty(targetList) || targetList.size() != blogId.size())
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        imageService.removeBlogImagesByBlogReviewList(targetList);
        reviewRepository.removeListPermanently(targetList.stream().map(BlogReview::getId).collect(Collectors.toList()));
    }
}
