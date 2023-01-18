package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogReviewRepository extends JpaRepository<BlogReview, Long> {
}
