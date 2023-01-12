package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.repository.BlogReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogReviewService {

    private final BlogReviewRepository repository;

    public Long uploadBlog(BlogRequestDto blogRequestDto,Long memberId){
        BlogReview newReview = BlogReview.builder()
                .title(blogRequestDto.getTitle())
                .content(blogRequestDto.getContent())
                .viewDate(LocalDate.parse(blogRequestDto.getViewDate()))
                .transportation(blogRequestDto.getTransportation())
                .revisit(blogRequestDto.getRevisit())
                .congestion(blogRequestDto.getCongestion())
                .viewCount(0)
                .likeCount(0)
                .isDeleted(false)
                .member(memberId)
                .exhibition(3L)
                .build();
        return repository.save(newReview).getId();
    }


    public void updateBlogReview(BlogRequestDto blogRequestDto, Long blogId, Long memberId) {
        BlogReview preBlogReview = verifyRequester(blogId,memberId);
        preBlogReview.updateReview(blogRequestDto);
        repository.save(preBlogReview);
    }

    private BlogReview findBlogReview(Long blogId){
        return repository.findById(blogId).orElseThrow(() -> { //존재 하지 않는 글 -> 404 Not Found
            throw new NoSuchElementException();
        });
    }
    private BlogReview verifyRequester(Long blogId,Long memberId){
        BlogReview review = repository.findById(blogId).orElseThrow(() -> { //블로그 글이 조회 되지 않는 경우 -> 404 Not Found 로 변경할 예정
           throw new NoSuchElementException();
        });
        if(!review.getMember().equals(memberId)){ //권한이 없는 글에 접근하는 경우 -> 403 Forbidden 로 변경할 예정
            throw new NoSuchElementException();
        }
        return review;
    }
}
