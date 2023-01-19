package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.test_user.TestMember;
import com.myalley.test_user.TestMemberRepository;

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
    private final TestMemberRepository mRepository;

    public BlogReview createBlog(BlogRequestDto blogRequestDto, Long memberId){
        //멤버를 id로 받아오는 경우만 해당 됨. 전시회는 id로 받아올 것이기 때문에 전시회 부분도 추가로 해주기!
        TestMember writer = mRepository.findById(memberId).orElseThrow(() ->{
            throw new CustomException(BlogReviewExceptionType.BLOG_BAD_REQUEST);
        });
        //중복 허용하기로함 1.18
        //duplicateCheckBlogReview(memberId,blogRequestDto.getExhibitionId());
        BlogReview newReview = BlogReview.builder()
                .title(blogRequestDto.getTitle())
                .content(blogRequestDto.getContent())
                .viewDate(LocalDate.parse(blogRequestDto.getViewDate()))
                .transportation(blogRequestDto.getTransportation())
                .revisit(blogRequestDto.getRevisit())
                .congestion(blogRequestDto.getCongestion())
                .testMember(writer)
                .exhibition(blogRequestDto.getExhibitionId()) //여기도 나중엔 객체로 넣어 주어야 합니둥
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
        if(!review.getTestMember().getId().equals(memberId)){ //403 : 권한이 없는 글에 접근하는 경우
            throw new CustomException(BlogReviewExceptionType.BLOG_FORBIDDEN);
        }
        return review;
    }
}
