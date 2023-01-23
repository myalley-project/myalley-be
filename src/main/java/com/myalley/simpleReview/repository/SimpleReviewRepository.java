package com.myalley.simpleReview.repository;

import com.myalley.simpleReview.domain.SimpleReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleReviewRepository extends JpaRepository<SimpleReview, Long> {
}
