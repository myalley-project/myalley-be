package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.test_user.TestMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogReviewRepository extends JpaRepository<BlogReview, Long> {
    Page<BlogReview> findAll(Pageable pageable); //최신순, 조회수 순
    Page<BlogReview> findAllByTestMember(TestMember member,Pageable pageable); //내 글 조회
}
