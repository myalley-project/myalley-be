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
    public BlogReview createBlog(BlogReview blogReview, Long memberId){
        //멤버를 id로 받아오는 경우만 해당 됨. 전시회는 id로 받아올 것이기 때문에 전시회 부분도 추가로 해주기!
        TestMember writer = mRepository.findById(memberId).orElseThrow(() ->{
            throw new CustomException(BlogReviewExceptionType.BLOG_BAD_REQUEST);
        });
        blogReview.setMember(writer);
        return repository.save(blogReview);
    }


    public BlogReview retrieveBlogReview(Long blogId){
        BlogReview blog = findBlogReview(blogId);
        blog.updateViewCount();
        BlogReview updateBlog = repository.save(blog);
        return updateBlog;
    }

    public void updateBlogReview(BlogReview postBlogReview, Long blogId, Long memberId) {
        BlogReview preBlogReview = verifyRequester(blogId,memberId);
        preBlogReview.updateReview(postBlogReview);
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
