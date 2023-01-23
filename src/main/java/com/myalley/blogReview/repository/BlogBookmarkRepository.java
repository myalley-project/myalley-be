package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.member.domain.Member;
import com.myalley.test_user.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogBookmarkRepository extends JpaRepository<BlogBookmark, Long> {
    Optional<BlogBookmark> findByMemberAndBlog(TestMember member, BlogReview blogReview);
}
