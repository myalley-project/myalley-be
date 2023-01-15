package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogReviewRepository extends JpaRepository<BlogReview, Long> {
    Optional<BlogReview> findByMemberAndExhibition(Long member, Long exhibition);
}
