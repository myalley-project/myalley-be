package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.test_user.TestMember;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogLikesRepository extends JpaRepository<BlogLikes, Long> {
    Optional<BlogLikes> findByTestMemberAndBlog(TestMember testMember, BlogReview blogReview);
}
