package com.myalley.blogReview_deleted.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview_deleted.domain.BlogReviewDeleted;
import com.myalley.blogReview_deleted.repository.BlogReviewDeletedRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BlogReviewDeletedService {
    private final BlogReviewDeletedRepository repository;

    public void addDeletedBlogReview(BlogReview target){
        BlogReviewDeleted deletedBlog = BlogReviewDeleted.builder()
                .title(target.getTitle())
                .content(target.getContent())
                .viewDate(target.getViewDate())
                .congestion(target.getCongestion())
                .transportation(target.getTransportation())
                .revisit(target.getRevisit())
                .createdAt(target.getCreatedAt())
                .deletedAt(LocalDateTime.now())
                .likeCount(target.getLikeCount())
                .viewCount(target.getViewCount())
                .member(target.getMember().getId())
                .exhibition(target.getExhibition())
                .build();
        repository.save(deletedBlog);
    }
}
