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

    public BlogReview createBlog(BlogRequestDto blogRequestDto, Long memberId){
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
        return repository.save(newReview);
    }

    public BlogReview retrieveBlogReview(Long blogId){
        BlogReview blog = findBlogReview(blogId);
        blog.updateViewCount();
        BlogReview updateBlog = repository.save(blog);
        return updateBlog;
    }

    public void updateBlogReview(BlogRequestDto blogRequestDto, Long blogId, Long memberId) {
        BlogReview preBlogReview = verifyRequester(blogId,memberId);
        preBlogReview.updateReview(blogRequestDto);
        repository.save(preBlogReview);
    }

    //삭제 전 올바른 요청인지 확인 (작성자가 보낸 요청인지)
    public BlogReview preVerifyBlogReview(Long blogId,Long memberId){
        BlogReview target = verifyRequester(blogId, memberId);
        return target;
    }

    public void deleteBlogReview(BlogReview target){
        repository.delete(target);
    }
/*
    public BlogReview deleteBlogReview(Long blogId,Long memberId){
        BlogReview target = verifyRequester(blogId, memberId);
        repository.delete(target);
        return target;
    }

 */
    
    //1. 존재하는 글인지 확인
    private BlogReview findBlogReview(Long blogId){
        BlogReview blog = repository.findById(blogId).orElseThrow(() -> { //404 : 존재 하지 않는 글
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        return blog;
    }
    
    //2. 작성자인지 확인
    private BlogReview verifyRequester(Long blogId,Long memberId){
        BlogReview review = repository.findById(blogId).orElseThrow(() -> { //404 : 블로그 글이 조회 되지 않는 경우
           throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        if(!review.getMember().equals(memberId)){ //403 : 권한이 없는 글에 접근하는 경우
            throw new CustomException(BlogReviewExceptionType.BLOG_FORBIDDEN);
        }
        return review;
    }
    
    //3. 리뷰 중복 작성을 막기 위해 memberId와 exhibitionId를 받아 존재하는지 확인
    private void duplicateCheckBlogReview(Long member, Long exhibition){ //409 : 이미 작성한 경우
        repository.findByMemberAndExhibition(member, exhibition).ifPresent( e -> {
            throw new CustomException(BlogReviewExceptionType.BLOG_CONFLICT);
        });
    }


}
