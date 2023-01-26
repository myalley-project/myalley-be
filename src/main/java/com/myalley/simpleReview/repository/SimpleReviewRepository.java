package com.myalley.simpleReview.repository;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleReviewRepository extends JpaRepository<SimpleReview, Long> {
    Page<SimpleReview> findAllByExhibition(Exhibition exhibition, Pageable pageable);
    Page<SimpleReview> findAllByMember(Member member, Pageable pageable);
}
