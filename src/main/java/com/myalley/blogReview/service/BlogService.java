package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.Blog;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository repository;

    public Long uploadBlog(BlogRequestDto blogRequestDto,Long memberId){
        LocalDate today = LocalDate.now();
        Blog newReview = Blog.builder()
                .title(blogRequestDto.getTitle())
                .content(blogRequestDto.getContent())
                .viewDate(LocalDate.parse(blogRequestDto.getViewDate()))
                .transportation(blogRequestDto.getTransportation())
                .revisit(blogRequestDto.getRevisit())
                .congestion(blogRequestDto.getCongestion())
                .createdAt(today)
                .modifiedAt(today)
                .viewCount(0)
                .likeCount(0)
                .isDeleted(false)
                .member(memberId)
                .exhibition(3L)
                .build();
        return repository.save(newReview).getId();
    }


}
