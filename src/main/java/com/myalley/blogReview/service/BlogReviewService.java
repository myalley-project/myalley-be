package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BlogReviewService {

    private final BlogReviewRepository repository;

    public Long uploadBlog(BlogRequestDto blogRequestDto, Long memberId){
        duplicateCheckBlogReview(memberId,blogRequestDto.getExhibitionId());
        BlogReview newReview = BlogReview.builder()
                .title(blogRequestDto.getTitle())
                .content(blogRequestDto.getContent())
                .viewDate(LocalDate.parse(blogRequestDto.getViewDate()))
                .transportation(blogRequestDto.getTransportation())
                .revisit(blogRequestDto.getRevisit())
                .congestion(blogRequestDto.getCongestion())
                .viewCount(0)
                .likeCount(0)
                .member(memberId)
                .exhibition(blogRequestDto.getExhibitionId())
                .build();
        return repository.save(newReview).getId();
    }


    public void updateBlogReview(BlogRequestDto blogRequestDto, Long blogId, Long memberId) {
        BlogReview preBlogReview = verifyRequester(blogId,memberId);
        preBlogReview.updateReview(blogRequestDto);
        repository.save(preBlogReview);
    }
    public BlogReview deleteBlogReview(Long blogId,Long memberId){
        BlogReview target = verifyRequester(blogId, memberId);
        repository.delete(target);
        return target;
    }

    private BlogReview findBlogReview(Long blogId){
        return repository.findById(blogId).orElseThrow(() -> { //404 : 존재 하지 않는 글
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
    }
    private BlogReview verifyRequester(Long blogId,Long memberId){
        BlogReview review = repository.findById(blogId).orElseThrow(() -> { //404 : 블로그 글이 조회 되지 않는 경우
           throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        if(!review.getMember().equals(memberId)){ //403 : 권한이 없는 글에 접근하는 경우
            throw new CustomException(BlogReviewExceptionType.BLOG_FORBIDDEN);
        }
        return review;
    }
    private void duplicateCheckBlogReview(Long member, Long exhibition){ //409 : 이미 작성한 경우
        repository.findByMemberAndExhibition(member, exhibition).ifPresent( e -> {
            throw new CustomException(BlogReviewExceptionType.BLOG_CONFLICT);
        });
    }
}
