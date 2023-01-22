package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.test_user.TestMember;
import com.myalley.test_user.TestMemberRepository;

import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

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

    public Page<BlogReview> retrieveBlogReviewList(int pageNo,String orderType){
        //1. 만약 orderType이 null인 경우, Recent 를 담음
        Page<BlogReview> blogReviewList;
        //2. 페이지네이션 넣을 정보 담기 pageNo, 한 페이지당 개수 9개, 정렬은 orderType (orderType = User, ViewCount, Recent)
        if(orderType.equals("User")) {
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("id").descending());
            blogReviewList = repository.findAllByTestMember(pageRequest);
        } else if(orderType.equals("ViewCount")) {
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("viewCount").descending());
            blogReviewList = repository.findAllById(pageRequest);
        } else{
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("id").descending());
            blogReviewList = repository.findAllById(pageRequest);
        }
        //3. 페이지 정보 구성 및 데이터 작업은 Mapper로
        return blogReviewList;
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
